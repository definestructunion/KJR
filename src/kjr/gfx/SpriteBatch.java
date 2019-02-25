package kjr.gfx;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class SpriteBatch extends Renderer
{
    public final static int INDICES_SIZE            = 6;

    public final static int SHADER_VERTEX_INDEX     = 0;
    public final static int SHADER_UV_INDEX         = 1;
    public final static int SHADER_TID_INDEX        = 2;
    public final static int SHADER_COLOR_INDEX      = 3;

    public final static int SHADER_VERTEX_SIZE      = (3 * 4);
    public final static int SHADER_UV_SIZE          = (2 * 4);
    public final static int SHADER_TID_SIZE         = (1 * 4);
    public final static int SHADER_COLOR_SIZE       = (1 * 4);

    public final static int RENDERER_MAX_TEXTURES   = 32;
    public final static int RENDERER_MAX_SPRITES    = 60000;
    public final static int RENDERER_VERTEX_SIZE    = SHADER_VERTEX_SIZE + SHADER_UV_SIZE + SHADER_COLOR_SIZE + SHADER_TID_SIZE;
    public final static int RENDERER_SPRITE_SIZE    = RENDERER_VERTEX_SIZE * 4;
    public final static int RENDERER_BUFFER_SIZE    = RENDERER_SPRITE_SIZE * RENDERER_MAX_SPRITES;
    public final static int RENDERER_INDICES_SIZE   = RENDERER_MAX_SPRITES * INDICES_SIZE;

    private int vao;
    private int vbo;
    private IndexBuffer ibo;
    private int indexCount;
    private FloatBuffer buffer;
    private ArrayList<Float> textureSlots = new ArrayList<Float>(RENDERER_MAX_TEXTURES);

    private ArrayList<Font> fonts = new ArrayList<Font>();
    private Font fontsBack = null;

    public SpriteBatch(int tile_size)
    {
        super(tile_size);
        init();
    }

    @Override public void delete()
    {
        super.delete();
        ibo.delete();
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
    }

    private void init()
    {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, RENDERER_BUFFER_SIZE, GL_DYNAMIC_DRAW);

        glEnableVertexAttribArray(SHADER_VERTEX_INDEX);
        glEnableVertexAttribArray(SHADER_UV_INDEX);
        glEnableVertexAttribArray(SHADER_TID_INDEX);
        glEnableVertexAttribArray(SHADER_COLOR_INDEX);

        glVertexAttribPointer(SHADER_VERTEX_INDEX, 3, GL_FLOAT, false, RENDERER_VERTEX_SIZE, 0);
        glVertexAttribPointer(SHADER_UV_INDEX, 2, GL_FLOAT, false, RENDERER_VERTEX_SIZE, SHADER_VERTEX_SIZE);
        glVertexAttribPointer(SHADER_TID_INDEX, 1, GL_FLOAT, false, RENDERER_VERTEX_SIZE, SHADER_VERTEX_SIZE + SHADER_UV_SIZE);
        glVertexAttribPointer(SHADER_COLOR_INDEX, 4, GL_UNSIGNED_BYTE, true, RENDERER_VERTEX_SIZE, SHADER_VERTEX_SIZE + SHADER_UV_SIZE + SHADER_TID_SIZE);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        int[] indices = new int[RENDERER_INDICES_SIZE];

        int offset = 0;
        for (int i = 0; i < RENDERER_INDICES_SIZE; i += INDICES_SIZE)
        {
            indices[i + 0] = offset + 0;
            indices[i + 1] = offset + 1;
            indices[i + 2] = offset + 2;

            indices[i + 3] = offset + 2;
            indices[i + 4] = offset + 3;
            indices[i + 5] = offset + 0;

            offset += 4;
        }

        ibo = new IndexBuffer(indices, RENDERER_INDICES_SIZE);

        glBindVertexArray(0);
    }

    public void pushFont(Font font)
    {
        fonts.add(font);
        fontsBack = font;
    }

    public void popFont()
    {
        int size = fonts.size();

        if(size > 0)
        {
            fonts.remove(size - 1);
            fontsBack = fonts.get(size - 2);
        }
    }

    public void setSortModeLayered()
    {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_ALPHA_TEST);
        //glDepthFunc(GL_GREATER);
        glAlphaFunc(GL_GREATER, 0.0f);
    }

    public void setSortModeDeferred()
    {
        glDisable(GL_DEPTH_TEST);
        //glDisable(GL_ALPHA_TEST);
    }

    public void begin()
    {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        buffer = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY).asFloatBuffer();
    }

    public void draw(Texture texture, Colour colour, int x, int y, float layer)
    {
        float posX = tilePosX(x);
        float posY = tilePosY(y);
        drawSquare(texture.getID(), colour.hex(), posX, posY, layer);
    }

    public void draw(Colour colour, int x, int y, float layer)
    {
        float posX = tilePosX(x);
        float posY = tilePosY(y);
        drawSquare(0, colour.hex(), posX, posY, layer);
    }

    public void draw(char c, Colour colour, int x, int y, float layer)
    {
        float posX = tilePosX(x);
        float posY = tilePosY(y);

        flushIfNeeded(INDICES_SIZE);
        float slot = getSlot(textureSlots, fontsBack.getID());
        Glyph glyph = fontsBack.getGlyph(c);
        drawGlyph(glyph, slot, colour.hex(), posX, posY, layer);
    }

    public void drawFree(Texture texture, Colour colour, int x, int y, float layer)
    {
        drawSquare(texture.getID(), colour.hex(), x, y, layer);
    }

    public void drawString(String text, Colour colour, int x, int y, float layer)
    {
        flushIfNeeded(INDICES_SIZE * text.length());
        float posX = tilePosX(x);
        float posY = tilePosY(y);
        float slot = getSlot(textureSlots, fontsBack.getID());
        Glyph glyph = null;

        for(int i = 0; i < text.length(); ++i)
        {
            char c = text.charAt(i);

            if(c == '\n')
            {
                posX = tilePosX(x);
                posY += tileSize;
                continue;
            }

            glyph = fontsBack.getGlyph(c);
            drawGlyph(glyph, slot, colour.hex(), posX, posY, layer);
            posX += tileSize;
        }
    }

    public void drawStringFree(String text, Colour colour, int x, int y, float layer)
    {
        flushIfNeeded(INDICES_SIZE * text.length());
        float posX = x;
        float posY = y;
        float slot = getSlot(textureSlots, fontsBack.getID());
        Glyph glyph = null;

        for(int i = 0; i < text.length(); ++i)
        {
            //flushIfNeeded(INDICES_SIZE);
            char c = text.charAt(i);

            if(c == '\n')
            {
                posX = tilePosX(x);
                posY += tileSize;
                continue;
            }

            glyph = fontsBack.getGlyph(c);
            drawGlyph(glyph, slot, colour.hex(), posX, posY, layer);
            posX += tileSize;
        }
    }

    private void drawSquare(float id, float colour, float x, float y, float layer)
    {
        flushIfNeeded(INDICES_SIZE);
        float slot = getSlot(textureSlots, id);

        fillBuffer(slot, colour, x, y, layer, 0, 0);
        fillBuffer(slot, colour, x, y + tileSize, layer, 0, 1);
        fillBuffer(slot, colour, x + tileSize, y + tileSize, layer, 1, 1);
        fillBuffer(slot, colour, x + tileSize, y, layer, 1, 0);
        indexCount += INDICES_SIZE;
    }

    private void drawGlyph(Glyph glyph, float slot, float colour, float x, float y, float layer)
    {
        flushIfNeeded(INDICES_SIZE);
        fillBuffer(slot, colour, x, y, layer, glyph.uv[0].x, glyph.uv[0].y);
        fillBuffer(slot, colour, x, y + tileSize, layer, glyph.uv[1].x, glyph.uv[1].y);
        fillBuffer(slot, colour, x + tileSize, y + tileSize, layer, glyph.uv[2].x, glyph.uv[2].y);
        fillBuffer(slot, colour, x + tileSize, y, layer, glyph.uv[3].x, glyph.uv[3].y);
        indexCount += INDICES_SIZE;
    }

    private void fillBuffer(float slot, float colour, float x, float y, float layer, float u, float v)
    {
        buffer.put(x).put(y).put(layer);
        buffer.put(u).put(v);
        buffer.put(slot);
        buffer.put(colour);
    }

    public void end()
    {
        glUnmapBuffer(GL_ARRAY_BUFFER);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void flush()
    {
        for(int i = 0; i < textureSlots.size(); ++i)
        {
            // first activate the texture
			glActiveTexture(GL_TEXTURE0 + i);
			// then set the texture equal to the texture ID
			glBindTexture(GL_TEXTURE_2D, (int) textureSlots.get(i).floatValue());
        }

        glBindVertexArray(vao);
        ibo.bind();

        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);

        ibo.unbind();
        glBindVertexArray(0);
        indexCount = 0;
        buffer.clear();
    }
    
    private void flushIfNeeded(int expectedIndexCountIncrease)
    {
        if(indexCount + expectedIndexCountIncrease >= RENDERER_INDICES_SIZE)
        {
            end();
            flush();
            begin();
        }

        else if(textureSlots.size() >= RENDERER_MAX_TEXTURES)
        {
            end();
            flush();
            begin();
            textureSlots.clear();
            textureSlots.ensureCapacity(RENDERER_MAX_TEXTURES);
        }
    }

    private float tilePosX(int x)
    {
        return x * tileSize;
    }

    private float tilePosY(int y)
    {
        return y * tileSize;
    }
}

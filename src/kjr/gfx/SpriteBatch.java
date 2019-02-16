package kjr.gfx;

import kjr.gfx.IndexBuffer;
import kjr.gfx.Font;

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
    private int index_count;
    private FloatBuffer buffer;
    private ArrayList<Float> texture_slots = new ArrayList<Float>(RENDERER_MAX_TEXTURES);

    private ArrayList<Font> fonts = new ArrayList<Font>();
    private Font fonts_back = null;

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
        fonts_back = fonts.get(fonts.size() - 1);
    }

    // only pops if size > 1
    public void popFont()
    {
        if(fonts.size() > 1)
        {
            fonts.remove(fonts.get(fonts.size() - 1));
            fonts_back = fonts.get(fonts.size() - 1);
        }
    }

    public void setSortModeLayered()
    {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER, 0.0f);
    }

    public void setSortModeDeferred()
    {
        glDisable(GL_DEPTH_TEST);
    }

    public void begin()
    {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        buffer = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY).asFloatBuffer();
    }

    private void draw(Texture texture, Colour color, float x, float y, float layer, float width, float height)
    {
        flushIfNeeded(INDICES_SIZE);

        float id = (texture == null) ? 0 : texture.getID();
        float slot = getSlot(texture_slots, id);

        buffer.put(x).put(y).put(layer);
        buffer.put(0).put(0);
        buffer.put(slot);
        buffer.put(color.hex());

        buffer.put(x).put(y + height).put(layer);
        buffer.put(0).put(1);
        buffer.put(slot);
        buffer.put(color.hex());

        buffer.put(x + width).put(y + height).put(layer);
        buffer.put(1).put(1);
        buffer.put(slot);
        buffer.put(color.hex());

        buffer.put(x + width).put(y).put(layer);
        buffer.put(1).put(0);
        buffer.put(slot);
        buffer.put(color.hex());

        index_count += INDICES_SIZE;
    }

    private void drawGlyph(Glyph glyph, float glyph_slot, Colour colour,float glyph_dimensions, float x, float y, float layer)
    {
        buffer.put(x).put(y).put(layer);
        buffer.put(glyph.uv[0].x).put(glyph.uv[0].y);
        buffer.put(glyph_slot);
        buffer.put(colour.hex());

        buffer.put(x).put(y + glyph_dimensions).put(layer);
        buffer.put(glyph.uv[1].x).put(glyph.uv[1].y);
        buffer.put(glyph_slot);
        buffer.put(colour.hex());

        buffer.put(x + glyph_dimensions).put(y + glyph_dimensions).put(layer);
        buffer.put(glyph.uv[2].x).put(glyph.uv[2].y);
        buffer.put(glyph_slot);
        buffer.put(colour.hex());

        buffer.put(x + glyph_dimensions).put(y).put(layer);
        buffer.put(glyph.uv[3].x).put(glyph.uv[3].y);
        buffer.put(glyph_slot);
        buffer.put(colour.hex());

        index_count += INDICES_SIZE;
    }

    private void drawGlyphsArray(char[] glyphs, Colour colour, float scale, float x, float y, float layer)
    {
        flushIfNeeded(INDICES_SIZE * glyphs.length);
        float slot = getSlot(texture_slots, fonts_back.getID());
        float glyph_dimensions = fonts_back.getGlyphDimensions() * scale;

        for(int i = 0; i < glyphs.length; ++i)
        {
            Glyph glyph = fonts_back.getGlyph(glyphs[i]);
            drawGlyph(glyph, slot, colour, glyph_dimensions, x, y, layer);
            x += glyph_dimensions;
        }
    }

    public void drawGlyph(char glyph_char, Colour colour, float scale, int x, int y, float layer)
    {
        flushIfNeeded(INDICES_SIZE);
        float pos_x = tilePosX(x);
        float pos_y = tilePosY(y);
        float slot = getSlot(texture_slots, fonts_back.getID());
        Glyph glyph = fonts_back.getGlyph(glyph_char);
        drawGlyph(glyph, slot, colour, fonts_back.getGlyphDimensions() * scale, pos_x, pos_y, layer);
    }

    public void drawGlyphFree(char glyph_char, Colour colour, float scale, int x, int y, float layer)
    {
        flushIfNeeded(INDICES_SIZE);
        float slot = getSlot(texture_slots, fonts_back.getID());
        Glyph glyph = fonts_back.getGlyph(glyph_char);
        drawGlyph(glyph, slot, colour, fonts_back.getGlyphDimensions() * scale, x, y, layer);
    }

    public void drawGlyphs(Colour colour, float scale, int x, int y, float layer, char... glyphs)
    {
        float pos_x = tilePosX(x);
        float pos_y = tilePosY(y);
        drawGlyphsArray(glyphs, colour, scale, pos_x, pos_y, layer);
    }

    public void drawGlyphs(char[] glyphs, Colour colour, float scale, int x, int y, float layer)
    {
        float pos_x = tilePosX(x);
        float pos_y = tilePosY(y);
        drawGlyphsArray(glyphs, colour, scale, pos_x, pos_y, layer);
    }

    public void drawGlyphsFree(Colour colour, float scale, int x, int y, float layer, char... glyphs)
    {
        drawGlyphsArray(glyphs, colour, scale, x, y, layer);
    }

    public void drawGlyphsFree(char[] glyphs, Colour colour, float scale, int x, int y, float layer)
    {
        drawGlyphsArray(glyphs, colour, scale, x, y, layer);
    }

    public void drawString(String text, Colour colour, float scale, int x, int y, float layer)
    {
        flushIfNeeded(INDICES_SIZE);
        float pos_x = tilePosX(x);
        float pos_y = tilePosY(y);
        float slot = getSlot(texture_slots, fonts_back.getID());
        float dim = fonts_back.getGlyphDimensions() * scale;

        for(int i = 0; i < text.length(); ++i)
        {
            char c = text.charAt(i);

            if(c == '\n')
            {
                pos_x = tilePosX(x);
                pos_y += dim;
                continue;
            }

            Glyph glyph = fonts_back.getGlyph(c);
            drawGlyph(glyph, slot, colour, dim, pos_x, pos_y, layer);
            pos_x += dim;
        }
    }

    public void drawStringFree(String text, Colour colour, float scale, int x, int y, float layer)
    {
        flushIfNeeded(INDICES_SIZE);
        float slot = getSlot(texture_slots, fonts_back.getID());
        float dim = fonts_back.getGlyphDimensions() * scale;
        float pos_x = x;
        float pos_y = y;

        for(int i = 0; i < text.length(); ++i)
        {
            char c = text.charAt(i);

            if(c == '\n')
            {
                pos_x = x;
                pos_y += dim;
                continue;
            }

            Glyph glyph = fonts_back.getGlyph(c);
            drawGlyph(glyph, slot, colour, dim, pos_x, pos_y, layer);
            x += dim;
        }
    }

    public void draw(Texture texture, Colour color, int x, int y, float layer)
    {
        float pos_x = tilePosX(x);
        float pos_y = tilePosY(y);
        draw(texture, color, pos_x, pos_y, layer, tile_size, tile_size);
    }

    public void draw(Colour color, int x, int y, float layer)
    {
        float pos_x = tilePosX(x);
        float pos_y = tilePosY(y);
        draw(null, color, pos_x, pos_y, layer, tile_size, tile_size);
    }

    public void drawFree(Texture texture, Colour color, Rect rect, float layer)
    {
        draw(texture, color, rect.x, rect.y, layer, rect.width, rect.height);
    }

    public void drawFree(Colour color, Rect rect, float layer)
    {
        draw(null, color, rect.x, rect.y, layer, rect.width, rect.height);
    }

    public void end()
    {
        glUnmapBuffer(GL_ARRAY_BUFFER);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void flush()
    {
        for(int i = 0; i < texture_slots.size(); ++i)
        {
            // first activate the texture
			glActiveTexture(GL_TEXTURE0 + i);
			// then set the texture equal to the texture ID
			glBindTexture(GL_TEXTURE_2D, (int)texture_slots.get(i).floatValue());
        }

        glBindVertexArray(vao);
        ibo.bind();

        glDrawElements(GL_TRIANGLES, index_count, GL_UNSIGNED_INT, 0);

        ibo.unbind();
        glBindVertexArray(0);
        index_count = 0;
    }
    
    private void flushIfNeeded(int expected_index_count_increase)
    {
        if(index_count + expected_index_count_increase >= RENDERER_INDICES_SIZE)
        {
            end();
            flush();
            begin();
        }

        else if(texture_slots.size() >= RENDERER_MAX_TEXTURES)
        {
            end();
            flush();
            begin();
            texture_slots.clear();
            texture_slots.ensureCapacity(RENDERER_MAX_TEXTURES);
        }
    }

    private float tilePosX(int x)
    {
        return x * tile_size;
    }

    private float tilePosY(int y)
    {
        return y * tile_size;
    }
}

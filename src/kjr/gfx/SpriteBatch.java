package kjr.gfx;

import kjr.gfx.IndexBuffer;
import kjr.math.Vec4;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.stb.STBTTAlignedQuad;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class SpriteBatch extends Renderer
{
    public final static int INDICES_SIZE            = 6;

    public final static int SHADER_IS_TEXT_INDEX    = 0;
    public final static int SHADER_VERTEX_INDEX     = 1;
    public final static int SHADER_UV_INDEX         = 2;
    public final static int SHADER_TID_INDEX        = 3;
    public final static int SHADER_COLOR_INDEX      = 4;

    public final static int SHADER_IS_TEXT_SIZE     = (1 * 4);
    public final static int SHADER_VERTEX_SIZE      = (3 * 4);
    public final static int SHADER_UV_SIZE          = (2 * 4);
    public final static int SHADER_TID_SIZE         = (1 * 4);
    public final static int SHADER_COLOR_SIZE       = (1 * 4);

    public final static int RENDERER_MAX_TEXTURES   = 32;
    public final static int RENDERER_MAX_SPRITES    = 16000;
    public final static int RENDERER_VERTEX_SIZE    = SHADER_VERTEX_SIZE + SHADER_UV_SIZE + SHADER_COLOR_SIZE + SHADER_TID_SIZE + SHADER_IS_TEXT_SIZE;
    public final static int RENDERER_SPRITE_SIZE    = RENDERER_VERTEX_SIZE * 4;
    public final static int RENDERER_BUFFER_SIZE    = RENDERER_SPRITE_SIZE * RENDERER_MAX_SPRITES;
    public final static int RENDERER_INDICES_SIZE   = RENDERER_MAX_SPRITES * INDICES_SIZE;

    private int vao;
    private int vbo;
    private IndexBuffer ibo;
    private int index_count;
    private FloatBuffer buffer;
    private ArrayList<Float> texture_slots = new ArrayList<Float>(RENDERER_MAX_TEXTURES);

    public SpriteBatch(TileData tile_info)
    {
        super(tile_info);
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

        glEnableVertexAttribArray(SHADER_IS_TEXT_INDEX);
        glEnableVertexAttribArray(SHADER_VERTEX_INDEX);
        glEnableVertexAttribArray(SHADER_UV_INDEX);
        glEnableVertexAttribArray(SHADER_TID_INDEX);
        glEnableVertexAttribArray(SHADER_COLOR_INDEX);

        glVertexAttribPointer(SHADER_IS_TEXT_INDEX, 1, GL_FLOAT, false, RENDERER_VERTEX_SIZE, 0);
        glVertexAttribPointer(SHADER_VERTEX_INDEX, 3, GL_FLOAT, false, RENDERER_VERTEX_SIZE, SHADER_IS_TEXT_SIZE);
        glVertexAttribPointer(SHADER_UV_INDEX, 2, GL_FLOAT, false, RENDERER_VERTEX_SIZE, SHADER_IS_TEXT_SIZE + SHADER_VERTEX_SIZE);
        glVertexAttribPointer(SHADER_TID_INDEX, 1, GL_FLOAT, false, RENDERER_VERTEX_SIZE, SHADER_IS_TEXT_SIZE + SHADER_VERTEX_SIZE + SHADER_UV_SIZE);
        glVertexAttribPointer(SHADER_COLOR_INDEX, 4, GL_UNSIGNED_BYTE, true, RENDERER_VERTEX_SIZE, SHADER_IS_TEXT_SIZE + SHADER_VERTEX_SIZE + SHADER_UV_SIZE + SHADER_TID_SIZE);

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

    private void draw(Texture texture, Vec4 color, float x, float y, float layer, float width, float height)
    {
        flushIfNeeded(INDICES_SIZE);

        int r = (int) (color.x * 255);
        int g = (int) (color.y * 255);
        int b = (int) (color.z * 255);
        int a = (int) (color.w * 255);

        float id = (texture == null) ? 0 : texture.getID();
        float slot = getSlot(texture_slots, id);

        float c = Float.intBitsToFloat((a << 0x0018 | b << 0x0010 | g << 0x0008 | r));

        buffer.put(0.0f);
        buffer.put(x).put(y).put(layer);
        buffer.put(0).put(0);
        buffer.put(slot);
        buffer.put(c);

        buffer.put(0.0f);
        buffer.put(x).put(y + height).put(layer);
        buffer.put(0).put(1);
        buffer.put(slot);
        buffer.put(c);

        buffer.put(0.0f);
        buffer.put(x + width).put(y + height).put(layer);
        buffer.put(1).put(1);
        buffer.put(slot);
        buffer.put(c);

        buffer.put(0.0f);
        buffer.put(x + width).put(y).put(layer);
        buffer.put(1).put(0);
        buffer.put(slot);
        buffer.put(c);

        index_count += INDICES_SIZE;
    }

    private void drawString(String text, Font font, Vec4 color, float x, float y, float layer)
    {
        flushIfNeeded(6 * text.length());

        STBTTAlignedQuad quad = STBTTAlignedQuad.malloc();
        FloatBuffer      xb   = memAllocFloat(1);
        FloatBuffer      yb   = memAllocFloat(1);

        int r = (int) (color.x * 255);
        int g = (int) (color.y * 255);
        int b = (int) (color.z * 255);
        int a = (int) (color.w * 255);

        float slot = getSlot(texture_slots, font.getID());
        float f_color = Float.intBitsToFloat((a << 0x0018 | b << 0x0010 | g << 0x0008 | r));

        xb.put(0, x);
        yb.put(0, y);

        char c = 0;
        for(int i = 0; i < text.length(); ++i)
        {
            c = text.charAt(i);
            
            font.getQuad(c, xb, yb, quad);
            buffer.put(1.0f);
            buffer.put(quad.x0()).put(quad.y0()).put(layer);
            buffer.put(quad.s0()).put(quad.t0());
            buffer.put(slot);
            buffer.put(f_color);

            buffer.put(1.0f);
            buffer.put(quad.x0()).put(quad.y1()).put(layer);
            buffer.put(quad.s0()).put(quad.t1());
            buffer.put(slot);
            buffer.put(f_color);

            buffer.put(1.0f);
            buffer.put(quad.x1()).put(quad.y1()).put(layer);
            buffer.put(quad.s1()).put(quad.t1());
            buffer.put(slot);
            buffer.put(f_color);

            buffer.put(1.0f);
            buffer.put(quad.x1()).put(quad.y0()).put(layer);
            buffer.put(quad.s1()).put(quad.t0());
            buffer.put(slot);
            buffer.put(f_color);

            index_count += INDICES_SIZE;
        }

        quad.free();
        memFree(xb);
        memFree(yb);
    }

    public void drawTile(Texture texture, Vec4 color, int x, int y, float layer)
    {
        float pos_x = (x * tiles.tile_size) + tiles.offset_x;
        float pos_y = (y * tiles.tile_size) + tiles.offset_y;
        draw(texture, color, pos_x, pos_y, layer, tiles.tile_size, tiles.tile_size);
    }

    public void drawTile(Vec4 color, int x, int y, float layer)
    {
        float pos_x = (x * tiles.tile_size) + tiles.offset_x;
        float pos_y = (y * tiles.tile_size) + tiles.offset_y;
        draw(null, color, pos_x, pos_y, layer, tiles.tile_size, tiles.tile_size);
    }

    public void drawFree(Texture texture, Vec4 color, Rect rect, float layer)
    {
        draw(texture, color, rect.x, rect.y, layer, rect.width, rect.height);
    }

    public void drawFree(Vec4 color, Rect rect, float layer)
    {
        draw(null, color, rect.x, rect.y, layer, rect.width, rect.height);
    }

    public void drawStringTile(String text, Font font, Vec4 color, int x, int y, float layer)
    {
        float pos_x = (x * tiles.tile_size) + tiles.offset_x;
        float pos_y = (y * tiles.tile_size) + tiles.offset_y;
        drawString(text, font, color, pos_x, pos_y, layer);
    }

    public void drawStringFree(String text, Font font, Vec4 color, float x, float y, float layer)
    {
        drawString(text, font, color, x, y, layer);
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
}

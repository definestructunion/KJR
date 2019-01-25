package kjr.gfx;

import kjr.gfx.IndexBuffer;
import kjr.math.Vec4;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.stb.STBTTAlignedQuad;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class LayerBatch extends Renderer
{
    private class Layer
    {
        public float[] buffer = new float[RENDERER_LAYER_MAX_SPRITES];
        public int index_count = 0;
        private int index = 0;

        public Layer()
        {

        }

        public void resizeIfNeeded(int put_count)
        {
            if(index + put_count >= buffer.length)
            {
                float[] new_buffer = new float[buffer.length + RENDERER_LAYER_MAX_SPRITES / 4];
                System.arraycopy(buffer, 0, new_buffer, 0, buffer.length);
                buffer = new_buffer;
            }
        }

        public void reset()
        {
            index_count = 0;
            Arrays.fill(buffer, 0.0f);
            index = 0;
        }

        public void addIndexCount(int index_additive)
        {
            index_count += index_additive;
        }

        public void put(float val)
        {
            buffer[index++] = val;
        }

        public void putAll(FloatBuffer f_buffer)
        {
            for(int i = 0; i < index; ++i)
            {
                f_buffer.put(buffer[i]);
            }
        }
    }

    public final static int INDICES_SIZE               = 6;

    public final static int SHADER_IS_TEXT_INDEX       = 0;
    public final static int SHADER_VERTEX_INDEX        = 1;
    public final static int SHADER_UV_INDEX            = 2;
    public final static int SHADER_TID_INDEX           = 3;
    public final static int SHADER_COLOR_INDEX         = 4;

    public final static int SHADER_IS_TEXT_SIZE        = (1 * 4);
    public final static int SHADER_VERTEX_SIZE         = (3 * 4);
    public final static int SHADER_UV_SIZE             = (2 * 4);
    public final static int SHADER_TID_SIZE            = (1 * 4);
    public final static int SHADER_COLOR_SIZE          = (1 * 4);

    public final static int BUFFER_PUT_COUNT           = 8;

    public final static int RENDERER_MAX_TEXTURES      = 32;
    public final static int RENDERER_MAX_SPRITES       = 60000;
    public final static int RENDERER_VERTEX_SIZE       = SHADER_VERTEX_SIZE + SHADER_UV_SIZE + SHADER_COLOR_SIZE + SHADER_TID_SIZE + SHADER_IS_TEXT_SIZE;
    public final static int RENDERER_SPRITE_SIZE       = RENDERER_VERTEX_SIZE * 4;
    public final static int RENDERER_BUFFER_SIZE       = RENDERER_SPRITE_SIZE * RENDERER_MAX_SPRITES;
    public final static int RENDERER_INDICES_SIZE      = RENDERER_MAX_SPRITES * INDICES_SIZE;

    public final static int RENDERER_LAYER_COUNT       = 10;
    public final static int RENDERER_LAYER_MAX_SPRITES = RENDERER_MAX_SPRITES / RENDERER_LAYER_COUNT;

    private int vao;
    private int vbo;
    private IndexBuffer ibo;
    private FloatBuffer buffer;
    private ArrayList<Float> texture_slots = new ArrayList<Float>(RENDERER_MAX_TEXTURES);

    private ArrayList<Layer> layers;

    public LayerBatch(TileData tile_info)
    {
        super(tile_info);
        init(RENDERER_LAYER_COUNT);
    }

    public LayerBatch(TileData tile_info, int layer_count)
    {
        super(tile_info);
        init(layer_count);
    }

    @Override public void delete()
    {
        super.delete();
        ibo.delete();
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
    }

    private void init(int layer_count)
    {
        layers = new ArrayList<Layer>(layer_count);
        for(int i = 0; i < layer_count; ++i)
        {
            layers.add(new Layer());
        }

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

    public void begin()
    {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        buffer = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY).asFloatBuffer();
    }

    public void drawTile(Texture texture, int x, int y, int layer, Vec4 color)
    {
        int pos_x = (x * tiles.tile_size) + tiles.offset_x;
        int pos_y = (y * tiles.tile_size) + tiles.offset_y;
        drawFree(texture, pos_x, pos_y, layer, color);
    }

    public void drawTile(Vec4 color, int x, int y, int layer)
    {
        int pos_x = (x * tiles.tile_size) + tiles.offset_x;
        int pos_y = (y * tiles.tile_size) + tiles.offset_y;
        drawFree(color, pos_x, pos_y, layer);
    }

    public void drawFree(Texture texture, int x, int y, int layer, Vec4 color)
    {
        Layer level = layers.get(layer);

        flushIfNeeded(level, 8 * 4);

        int r = (int) (color.x * 255);
        int g = (int) (color.y * 255);
        int b = (int) (color.z * 255);
        int a = (int) (color.w * 255);

        float slot = getSlot(texture_slots, texture.getID());

        float c = Float.intBitsToFloat((a << 0x0018 | b << 0x0010 | g << 0x0008 | r));

        level.put(0.0f);
        level.put(x); level.put(y); level.put(0);
        level.put(0); level.put(0);
        level.put(slot);
        level.put(c);

        level.put(0.0f);
        level.put(x); level.put(y + tiles.tile_size); level.put(0);
        level.put(0); level.put(1);
        level.put(slot);
        level.put(c);

        level.put(0.0f);
        level.put(x + tiles.tile_size); level.put(y + tiles.tile_size); level.put(0);
        level.put(1); level.put(1);
        level.put(slot);
        level.put(c);

        level.put(0.0f);
        level.put(x + tiles.tile_size); level.put(y); level.put(0);
        level.put(1); level.put(0);
        level.put(slot);
        level.put(c);

        level.addIndexCount(INDICES_SIZE);
    }

    public void drawFree(Vec4 color, int x, int y, int layer)
    {
        Layer level = layers.get(layer);

        flushIfNeeded(level, BUFFER_PUT_COUNT * 4);

        int r = (int) (color.x * 255);
        int g = (int) (color.y * 255);
        int b = (int) (color.z * 255);
        int a = (int) (color.w * 255);

        float slot = getSlot(texture_slots, 0.0f);

        float c = Float.intBitsToFloat((a << 0x0018 | b << 0x0010 | g << 0x0008 | r));

        level.put(0.0f);
        level.put(x); level.put(y); level.put(0);
        level.put(0); level.put(0);
        level.put(slot);
        level.put(c);

        level.put(0.0f);
        level.put(x); level.put(y + tiles.tile_size); level.put(0);
        level.put(0); level.put(1);
        level.put(slot);
        level.put(c);

        level.put(0.0f);
        level.put(x + tiles.tile_size); level.put(y + tiles.tile_size); level.put(0);
        level.put(1); level.put(1);
        level.put(slot);
        level.put(c);

        level.put(0.0f);
        level.put(x + tiles.tile_size); level.put(y); level.put(0);
        level.put(1); level.put(0);
        level.put(slot);
        level.put(c);

        level.addIndexCount(INDICES_SIZE);
    }

    public void drawStringTile(String text, Font font, int x, int y, int layer, Vec4 color)
    {
        int pos_x = (x * tiles.tile_size) + tiles.offset_x;
        int pos_y = (y * tiles.tile_size) + tiles.offset_y;
        drawStringFree(text, font, pos_x, pos_y, layer, color);
    }

    public void drawStringFree(String text, Font font, int x, int y, int layer, Vec4 color)
    {
        Layer level = layers.get(layer);
        flushIfNeeded(level, BUFFER_PUT_COUNT * (4 * text.length()));//6 * text.length());

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
            level.put(1.0f);
            level.put(quad.x0()); level.put(quad.y0()); level.put(0);
            level.put(quad.s0()); level.put(quad.t0());
            level.put(slot);
            level.put(f_color);

            level.put(1.0f);
            level.put(quad.x0()); level.put(quad.y1()); level.put(0);
            level.put(quad.s0()); level.put(quad.t1());
            level.put(slot);
            level.put(f_color);

            level.put(1.0f);
            level.put(quad.x1()); level.put(quad.y1()); level.put(0);
            level.put(quad.s1()); level.put(quad.t1());
            level.put(slot);
            level.put(f_color);

            level.put(1.0f);
            level.put(quad.x1()); level.put(quad.y0()); level.put(0);
            level.put(quad.s1()); level.put(quad.t0());
            level.put(slot);
            level.put(f_color);
            
            level.addIndexCount(INDICES_SIZE);
        }

        quad.free();
        memFree(xb);
        memFree(yb);
    }

    public void end()
    {
        for(int i = 0; i < layers.size(); ++i)
        {
            Layer layer = layers.get(i);
            layer.putAll(buffer);
        }

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

        glDrawElements(GL_TRIANGLES, getIndexCount(), GL_UNSIGNED_INT, 0);

        ibo.unbind();
        glBindVertexArray(0);

        for(int i = 0; i < layers.size(); ++i)
        {
            layers.get(i).reset();
        }
    }

    private int getIndexCount()
    {
        int index_count = 0;

        for(int i = 0; i < layers.size(); ++i)
        {
            index_count += layers.get(i).index_count;
        }

        return index_count;
    }
    
    private void flushIfNeeded(Layer layer, int put_count)
    {
        layer.resizeIfNeeded(put_count);

        if(texture_slots.size() >= RENDERER_MAX_TEXTURES)
        {
            end();
            flush();
            begin();
            texture_slots.clear();
            texture_slots.ensureCapacity(RENDERER_MAX_TEXTURES);
        }
    }
}

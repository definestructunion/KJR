package kjr.gfx;

import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_CLAMP_TO_EDGE;
import static kjr.lwjgl.IOUtil.ioResourceToByteBuffer;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.BufferUtils;

import org.lwjgl.stb.*;

public class Font
{
    private final static int CHAR_BUFFER_SIZE = 6;
    private final static int CHAR_BUFFER_COUNT = 128;

    // the size of the texture atlases
    // made public so the user can change them
    // if they so desire, but 512 is optimal
    public static int bitmap_width = 512;
    public static int bitmap_height = 512;

    public int size;

    FloatBuffer xb = memAllocFloat(1);
    FloatBuffer yb = memAllocFloat(1);

    private int id;
    private int width;
    private int height;
    private String file_path;
    private STBTTPackedchar.Buffer char_data;

    public Font(String file_path, int size)
    {
        this.file_path = file_path;
        this.size = size;
        this.width = bitmap_width;
        this.height = bitmap_height;
        this.id = load();
    }

    private int load()
    {
        final float[] scale = {
            size,
            size
        };

        int tex_id = glGenTextures();
        char_data = STBTTPackedchar.malloc(CHAR_BUFFER_SIZE * CHAR_BUFFER_COUNT);

        try(STBTTPackContext pc = STBTTPackContext.malloc())
        {
            ByteBuffer ttf = ioResourceToByteBuffer(file_path, 512 * 1024);
            ByteBuffer bitmap = BufferUtils.createByteBuffer(width * height);

            stbtt_PackBegin(pc, bitmap, width, height, 0, 1, 0);

            for(int i = 0; i < 2; ++i)
            {
                int p = (i * 3 + 0) * 128 + 32;
                char_data.limit(p + 95);
                char_data.position(p);
                stbtt_PackSetOversampling(pc, 1, 1);
                stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, char_data);    
                
                p = (i * 3 + 1) * 128 + 32;
                char_data.limit(p + 95);
                char_data.position(p);
                stbtt_PackSetOversampling(pc, 2, 2);
                stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, char_data); 

                p = (i * 3 + 2) * 128 + 32;
                char_data.limit(p + 95);
                char_data.position(p);
                stbtt_PackSetOversampling(pc, 3, 1);
                stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, char_data); 
            }

            char_data.clear();
            stbtt_PackEnd(pc);

            glBindTexture(GL_TEXTURE_2D, tex_id);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, width, height, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glBindTexture(GL_TEXTURE_2D, 0);
        }

        catch(IOException e)
        {
            throw new RuntimeException(e);
        }

        return tex_id;
    }

    public void getQuad(char c, FloatBuffer x, FloatBuffer y, STBTTAlignedQuad quad)
    {
        char_data.position(id * 128);
        stbtt_GetPackedQuad(char_data, width, height, c, x, y, quad, true);
    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void delete()
    {
        glDeleteTextures(id);
        char_data.free();
        memFree(xb);
        memFree(yb);
    }

    public int getID()
    {
        return id;
    }
}
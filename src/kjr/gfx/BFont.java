package kjr.gfx;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;

import kjr.math.Vec2;

public class BFont
{
    private int id;
    private String file_path;
    private int[] width = new int[1];
    private int[] height = new int[1];
    private int[] bits_per_pixel = new int[1];
    private Glyph[] glyphs = null;
    private int glyph_dimensions = 8;

    public BFont(String file_path, int glyph_dimensions)
    {
        this.file_path = file_path;
        this.glyph_dimensions = glyph_dimensions;
        this.glyphs = new Glyph[256];
        load();
        defaultGlyphsFill();
    }

    public BFont(String file_path, int glyph_dimensions, int glyph_count)
    {
        this.file_path = file_path;
        this.glyph_dimensions = glyph_dimensions;
        this.glyphs = new Glyph[glyph_count];
        load();
    }

    private void load()
    {
        stbi_set_flip_vertically_on_load(false);

        ByteBuffer buffer = stbi_load(file_path, width, height, bits_per_pixel, 4);

        id = glGenTextures();
        
        glBindTexture(GL_TEXTURE_2D, id);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glBindTexture(GL_TEXTURE_2D, 0);

        if(buffer != null)
        {
            stbi_image_free(buffer);
        }
    }

    private void defaultGlyphsFill()
    {
        int columns = (height[0] / glyph_dimensions);
        int rows = (width[0] / glyph_dimensions);
        float pos_x = 0;
        float pos_y = 0;

        for(int i = 0; i < rows * columns; ++i)
        {
            Vec2[] uvs = new Vec2[4];

            if(i % columns == 0 && i != 0)
            {
                pos_y += glyph_dimensions;
                pos_x = 0;
            }

            uvs[0] = new Vec2(pos_x / width[0], pos_y / height[0]);
            uvs[1] = new Vec2(pos_x / width[0], (pos_y + glyph_dimensions) / height[0]);
            uvs[2] = new Vec2((pos_x + glyph_dimensions) / width[0], (pos_y + glyph_dimensions) / height[0]);
            uvs[3] = new Vec2((pos_x + glyph_dimensions) / width[0], pos_y / height[0]);

            pos_x += glyph_dimensions;
            glyphs[i] = new Glyph(id, uvs);
        }
    }

    public int getGlyphDimensions()
    {
        return glyph_dimensions;
    }

    public Glyph getGlyph(char c)
    {
        return glyphs[c];
    }

    public Glyph getGlyph(int c)
    {
        return glyphs[c];
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
    }

    public int getID()
    {
        return id;
    }

    @Override public boolean equals(Object obj)
    {
        BFont t_obj = (BFont)obj;
        return file_path == t_obj.file_path && id == t_obj.id;
    }
}
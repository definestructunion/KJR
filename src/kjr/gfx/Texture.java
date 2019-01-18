package kjr.gfx;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;

/**
 * Image memory such as a sprite or picture
 */
public class Texture
{
    private int id;
    private String file_path;
    private int[] width = new int[1];
    private int[] height = new int[1];
    private int[] bits_per_pixel = new int[1];
    private ByteBuffer buffer;

    /**
     * Image memory such as a sprite or picture
     * Creates a texture
     * @param file_path the path to the image
     */
    public Texture(String file_path)
    {
        this.file_path = file_path;
        load();
    }

    /**
     * Loads the image to GPU memory and sets
     * our texture ID according to the OpenGL id
     * for this texture
     */
    private void load()
    {
        // flip the image upwards
        stbi_set_flip_vertically_on_load(false);

        // load the image into a buffer of memory
        // this function will set our width, height, and bits per pixel
        // according to the image's width, height, and bits per pixel
        // we will be using 4 channels for our images
        // R G B A
        buffer = stbi_load(file_path, width, height, bits_per_pixel, 4);

        // generate our texture into memory
        // in OpenGL
        // setting our texture's id to it
        // for future access
        id = glGenTextures();
        // bind our texture so we can set some parameters
        // and have it effect the texture we just created
        glBindTexture(GL_TEXTURE_2D, id);

        // these textures are 2D
		// the texture will have a minifying filter
		// and a magnification filter
		// and wrap the texture on the x and y axis
        // and clamp the textures to the edge
        
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        
        // this function moves the image data to our data (texture) over to the GPU
        // specify that it's a 2D texture
        // with the base detail (0)
        // with 8 bits of depth
        // our texture with width and height as texture dimensions
        // 0 must be the value of border
        // don't know why they even allow anything else
        // we may have to change GL_UNSIGNED_BYTE to GL_BYTE or GL_SHORT or something
        // since Java doesn't support unsigned variables
        // and our data is contained in buffer, so send over the buffer
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        // now that we've done all we needed to do with our texture
        // unbind it
        glBindTexture(GL_TEXTURE_2D, 0);

        // free our image if it's a valid
        // value
        if(buffer != null)
        {
            stbi_image_free(buffer);
        }
    }

    /**
     * Binds the texture to OpenGL
     */
    public void bind()
    {
        // bind our texture to OpenGL
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Unbinds the texture from OpenGL
     */
    public void unbind()
    {
        // unbind our texture from OpenGL
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
      <pre>
     * Warning: a memory leak will
     * occur unless this function
     * is explicitely called
     * 
     * Deletes the texture from memory
     * in OpenGL
     </pre>
     */
    public void delete()
    {
        // delete our texture from OpenGL
        glDeleteTextures(id);
    }

    public int getID()
    {
        return id;
    }

    @Override public boolean equals(Object obj)
    {
        Texture t_obj = (Texture)obj;
        return file_path == t_obj.file_path && id == t_obj.id;
    }
}
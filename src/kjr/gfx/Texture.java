package kjr.gfx;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * The {@code Texture} class represents an image to be used for rendering.
 * The {@code Texture} class uses <a href="https://github.com/nothings/stb">STB Image</a>, 
 * which supports the following formats:
 * <ul>
 * <li>PNG
 * <li>JPG
 * <li>TGA
 * <li>BMP
 * <li>PSD
 * <li>GIF
 * <li>HDR
 * <li>PIC
 * </ul>
 * Do note however that not all of these formats have been tested for, and may not work.
 * <p>
 * Due to OpenGL handling the memory allocation process, any allocation of the
 * {@code Texture} class must call delete() some time in the object's lifetime,
 * otherwise the memory will not be fully deallocated and will cause a memory leak.
 * <p>
 * The {@Texture} class contains a static {@code ArrayList} of Textures, which can be added to.
 * Upon exiting the {@code GameProgram}, the ArrayList will iterate and manually call delete(). Therefore, 
 * any {@code Texture}s added to the ArrayList does not require the user to manually call delete() themselves, 
 * and is preferred to use as there should only be one copy of a unique {@code Texture} object anyways.
 */
public class Texture
{
    static
    {
        textures = new ArrayList<Texture>();
    }

    private static ArrayList<Texture> textures;

    public static Texture add(String file_path)
    {
        Texture texture = new Texture(file_path);
        textures.add(texture);
        return texture;
    }

    public static Texture get(int index)
    {
        return textures.get(index);
    }

    public static Texture getBack()
    {
        return textures.get(textures.size() - 1);
    }

    public static void clean()
    {
        for(int i = 0; i < textures.size(); ++i)
        {
            textures.get(i).delete();
        }
    }

    private int id;
    private String file_path;
    private int[] width = new int[1];
    private int[] height = new int[1];
    private int[] bits_per_pixel = new int[1];

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
        ByteBuffer buffer = stbi_load(file_path, width, height, bits_per_pixel, 4);

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

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        Texture t_obj = (Texture)obj;
        return file_path.equals(t_obj.file_path) && id == t_obj.id;
    }

    @Override
    public int hashCode() { return id; }
}
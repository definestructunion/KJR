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
 * {@code Texture} class must call {@link #delete() delete} some time in the object's lifetime,
 * otherwise the memory will not be fully deallocated and will cause a memory leak.
 * <p>
 * The {@code Texture} class contains a static {@code ArrayList} of Textures, which can be added to.
 * Upon exiting the {@code GameProgram}, the ArrayList will iterate and manually call {@link #delete() delete}. Therefore,
 * any {@code Texture}s added to the ArrayList does not require the user to manually call {@link #delete() delete} themselves,
 * and is preferred to use as there should only be one copy of a unique {@code Texture} object anyways.
 */
public class Texture
{
    static
    {
        textures = new ArrayList<Texture>();
    }

    /**
     * The {@code ArrayList} of {@code Texture}s allows the automatic deallocation of
     * all elements in the list. When {@code GameProgram} calls  {@link kjr.base.GameProgram#clean() GameProgram.clean}, it will also call
     * {@code Texture.clean}.
     * <p>
     * If no elements are present in textures, nothing will be deleted.
     */
    private static ArrayList<Texture> textures;

    /**
     * Loads and returns a {@code Texture} based on the filePath given. This method also
     * adds the loaded {@code Texture} to {@link textures}.
     * @param filePath the path to the {@code Texture} to be loaded.
     * @return the {@code Texture} created based on the argument.
     */
    public static Texture add(String filePath)
    {
        Texture texture = new Texture(filePath);
        textures.add(texture);
        return texture;
    }

    /**
     * Returns a {@code Texture} based on the given index from the {@code ArrayList} {@link textures}.
     * In order to keep track of {@code Texture} indeces (IDs), the user can create an enum called
     * TextureNames (for example), and use ordinal(). If each {@code Texture} is added to represent the
     * enum, then knowing a {@code Texture} index can be kept track of much easier.
     * @param index the index of the desired {@code Texture} in {@link textures}
     * @return the {@code Texture} based on the {@link index}
     */
    public static Texture get(int index)
    {
        return textures.get(index);
    }

    /**
     * Returns the last element of {@link textures}. Will not check for {@link textures} size > 0.
     * @return the last element
     */
    public static Texture getBack()
    {
        return textures.get(textures.size() - 1);
    }

    /**
     * Iterates through {@link textures} and calls {@link #delete() delete} on each element in {@link textures}.
     * No {@code Texture} in {@link textures} needs to call {@link #delete() delete} manually by the user.
     */
    public static void clean()
    {
        for(int i = 0; i < textures.size(); ++i)
        {
            // does not deallocate the object in Java, just the data
            // in OpenGL
            textures.get(i).delete();
        }
    }

    /**
     * Integer ID for OpenGL to keep track of the {@code Texture}
     */
    private int id;

    /**
     * Keeps track of the {@code Texture}'s filePath
     */
    private String filePath;

    /**
     * The width of the {@code Texture} in pixels.
     */
    private Integer width = 0;

    /**
     * The height of the {@code Texture} in pixels.
     */
    private Integer height = 0;

    /**
     * The amount of bits in a pixel for colour data.
     */
    private Integer bitsPerPixel = 0;

    /**
     * Image memory such as a sprite or picture
     * Creates a texture
     * @param filePath the path to the image
     */
    public Texture(String filePath)
    {
        this.filePath = filePath;
        load();
    }

    /**
     * Loads the image to GPU memory and sets
     * our texture ID according to the OpenGL ID
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
        ByteBuffer buffer = stbi_load(filePath, width, height, bitsPerPixel, 4);

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
     * Binds the texture to OpenGL. This means that {@code Texture} will
     * be used until {@link #unbind() unbind} is called.
     */
    public void bind()
    {
        // bind our texture to OpenGL with our ID
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Unbinds the texture from OpenGL. This means that
     * the {@code Texture} won't be used anymore until {@link #bind() bind} is
     * called.
     */
    public void unbind()
    {
        // 0 is considered no texture
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Deletes the {@code Texture}'s memory from OpenGL, does not
     * deallocate the {@code Texture} from Java.
     * <p>
     * A memory leak will occur if a {@code Texture} gets collected
     * without having {@link #delete() delete} be called.
     */
    public void delete()
    {
        // delete our texture from OpenGL
        glDeleteTextures(id);
    }

    /**
     * Returns OpenGL's ID for this {@code Texture}'s ID.
     * @return ID for this {@code Texture}
     */
    public int getID()
    {
        return id;
    }

    /**
     * Returns whether or not the {@link filePath}s are the same.
     * @param obj The {@code Texture} for testing.
     * @return if the {@code Texture}s are the same.
     */
    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        Texture texObj = (Texture)obj;
        return filePath.equals(texObj.filePath);
    }

    /**
     * Returns this {@code Texture}'s {@link id} as a hashCode, as any {@code Texture} with
     * a different {@link id} is not equal.
     * @return
     */
    @Override
    public int hashCode() { return id; }
}
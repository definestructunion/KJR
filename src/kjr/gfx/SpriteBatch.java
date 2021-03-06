package kjr.gfx;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/**
 * Batches {@link Texture Textures}, {@link Font Fonts}, and {@link Colour Colours} into a single
 * buffer, which reduces draw calls and improving performance.
 * <p>
 * SpriteBatch requires OpenGL version 3.4 to be able to use SpriteBatch. If OpenGL version 3.4
 * is not available, then the shader that runs on SpriteBatch will not compile successfully.
 * <p>
 * SpriteBatch allows up to 32 textures, as specified by {@link SpriteBatch#RENDERER_MAX_SPRITES}. Once a unique {@link Texture Texture}
 * is added and was not added previously, a draw call will be made.
 */
public class SpriteBatch extends Renderer
{
    /**
     * The amount of indices in a single square/glyph.
     */
    public final static int INDICES_SIZE            = 6;

    /**
     * The index of the vertices info (x, y, z) in the {@link Shader Shader}.
     */
    public final static int SHADER_VERTEX_INDEX     = 0;

    /**
     * The index of the uv texture coordinates (between 0 and 1) in the {@link Shader Shader}.
     */
    public final static int SHADER_UV_INDEX         = 1;

    /**
     * The index of the texture ID in the {@link Shader Shader}.
     */
    public final static int SHADER_TID_INDEX        = 2;

    /**
     * The index of the colour (r, g, b, a) in the {@link Shader Shader}.
     */
    public final static int SHADER_COLOR_INDEX      = 3;

    /**
     * The size of the vertices info (x, y, z) in bytes.
     */
    public final static int SHADER_VERTEX_SIZE      = (3 * 4);

    /**
     * The size of the uv texture coordinates in bytes.
     */
    public final static int SHADER_UV_SIZE          = (2 * 4);

    /**
     * The size of the texture ID in bytes.
     */
    public final static int SHADER_TID_SIZE         = (1 * 4);

    /**
     * The size of the colour info (r, g, b, a) in bytes. Despite {@link Colour Colour} using
     * 4 floats, OpenGL reads the {@link Colour#hex() hex} value of the Colour, and each channel is
     * read as the size of a byte, which equals 4 bytes.
     */
    public final static int SHADER_COLOR_SIZE       = (1 * 4);

    /**
     * The amount of textures OpenGL allows to be bound at a single time/
     */
    public final static int RENDERER_MAX_TEXTURES   = 32;

    /**
     * The amount of sprites that can be pushed into a single draw buffer.
     */
    public final static int RENDERER_MAX_SPRITES    = 60000;

    /**
     * The size of a vertex in bytes.
     */
    public final static int RENDERER_VERTEX_SIZE    = SHADER_VERTEX_SIZE + SHADER_UV_SIZE + SHADER_COLOR_SIZE + SHADER_TID_SIZE;

    /**
     * The size of an entire sprite in bytes.
     */
    public final static int RENDERER_SPRITE_SIZE    = RENDERER_VERTEX_SIZE * 4;

    /**
     * The size of the {@link java.nio.FloatBuffer FloatBuffer} used to store sprite information.
     */
    public final static int RENDERER_BUFFER_SIZE    = RENDERER_SPRITE_SIZE * RENDERER_MAX_SPRITES;

    /**
     * The size of a index in bytes.
     */
    public final static int RENDERER_INDICES_SIZE   = RENDERER_MAX_SPRITES * INDICES_SIZE;

    /**
     * This {@link SpriteBatch SpriteBatch's} vertex array object.
     */
    private int vao;

    /**
     * This {@link SpriteBatch SpriteBatch's} vertex buffer object.
     */
    private int vbo;

    /**
     * This {@link SpriteBatch SpriteBatch's} index buffer object. Due to a square being rendered
     * as 2 triangles, there are 2 redundant indices. With {@link IndexBuffer IndexBuffer}, it allows
     * a square to be rendered with 4 indices, rather than 6.
     */
    private IndexBuffer ibo;

    /**
     * The amount of indices to be rendered. This informs OpenGL how many indices are to be drawn.
     */
    private int indexCount;

    /**
     * The buffer for our vertices.
     */
    private FloatBuffer buffer;

    /**
     * The {@link Texture Texture} array which holds the {@link Texture#id texture IDs}. OpenGL
     * is not able to use {@link Texture Texture}, OpenGL instead uses the {@link Texture#id ID} attached
     * to {@link Texture Texture}.
     */
    private ArrayList<Float> textureSlots = new ArrayList<Float>(RENDERER_MAX_TEXTURES);

    /**
     * Creates a new {@link SpriteBatch SpriteBatch} with the given tileSize.
     */
    public SpriteBatch(int tileSize)
    {
        super(tileSize);
        init();
    }

    /**
     * Deletes OpenGL resources that were allocated during the creation of {@link SpriteBatch SpriteBatch}. A memory leak
     * will occur if delete is not called in the object's lifetime.
     */
    @Override
    public void delete()
    {
        super.delete();
        ibo.delete();
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
    }

    /**
     * Initializes the {@link SpriteBatch SpriteBatch} by setting up the VBO, VAO, IBO, and the {@link Shader Shader}.
     */
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

    /**
     * Sets SpriteBatch to take the Z axis (layer) into consideration when drawing. This will mean that order of submission does
     * not matter unless the submitted sprites are on the same layer. If that is the case, then the objects submitted first will be
     * shown on top.
     */
    public void setSortModeLayered()
    {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_ALPHA_TEST);
        //glDepthFunc(GL_GREATER);
        glAlphaFunc(GL_GREATER, 0.0f);
    }

    /**
     * Sets SpriteBatch to not take the Z axis (layer) into consideration when drawing. This will mean that the order of submission
     * matters and objects submitted first will be behind/underneath the objects submitted last.
     */
    public void setSortModeDeferred()
    {
        glDisable(GL_DEPTH_TEST);
        //glDisable(GL_ALPHA_TEST);
    }

    /**
     * Starts the drawing process for the {@link SpriteBatch SpriteBatch}. An exception will be thrown if begin is either not called
     * or not called in the right order.
     * <p>
     * The order for drawing is:
     * <ul>
     * <li>begin
     * <li>{@link #end() end}
     * <li>{@link #flush() flush}
     * </ul>
     * The listed order must be followed strictly.
     */
    public void begin()
    {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        buffer = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY).asFloatBuffer();
    }

    /**
     * Draws a textured tile.
     * @param texture a {@link Texture texture} to use.
     * Will clamp the size down to {@link Renderer#tileSize tileSize}
     * for width and height.
     * @param colour a {@link Colour colour} to use.
     * Will set a tint on the {@link Texture texture}.
     * @param x coordinate that is adjusted to fit {@link Renderer#tileSize tileSize}.
     * @param y coordinate that is adjusted to fit {@link Renderer#tileSize tileSIze}.
     * @param layer z coordinate for layering. Will be ignored if {@link #setSortModeDeferred() 
     * deferred} rendering is enabled.
     */
    public void draw(Texture texture, Colour colour, int x, int y, float layer)
    {
        float posX = tilePosX(x);
        float posY = tilePosY(y);
        drawSquare(texture.getID(), colour.hex(), posX, posY, layer);
    }

    /**
     * Draws a coloured tile.
     * @param colour a {@link Colour colour} to use.
     * @param x coordinate that is adjusted to fit {@link Renderer#tileSize tileSize}.
     * @param y coordinate that is adjusted to fit {@link Renderer#tileSize tileSIze}.
     * @param layer z coordinate for layering. Will be ignored if {@link #setSortModeDeferred()
     * deferred} rendering is enabled.
     */
    public void draw(Colour colour, int x, int y, float layer)
    {
        float posX = tilePosX(x);
        float posY = tilePosY(y);
        drawSquare(0, colour.hex(), posX, posY, layer);
    }

    /**
     * Draws a glyph tile based on the {@link #fontsBack font} pushed into {@link SpriteBatch SpriteBatch}.
     * @param c the char to be drawn.
     * Will clamp the size down to {@link Renderer#tileSize tileSize}
     * for width and height.
     * @param colour a {@link Colour colour} to use.
     * Will set a tint on the {@link #fontsBack fontsBack Texture}.
     * @param x coordinate that is adjusted to fit {@link Renderer#tileSize tileSize}.
     * @param y coordinate that is adjusted to fit {@link Renderer#tileSize tileSIze}.
     * @param layer z coordinate for layering. Will be ignored if {@link #setSortModeDeferred()
     * deferred} rendering is enabled.
     */
    public void draw(char c, Colour colour, int x, int y, float layer)
    {
        float posX = tilePosX(x);
        float posY = tilePosY(y);

        flushIfNeeded(INDICES_SIZE);
        float slot = getSlot(textureSlots, fontsBack.getID());
        Glyph glyph = fontsBack.getGlyph(c);
        drawGlyph(glyph, slot, colour.hex(), posX, posY, layer);
    }

    /**
     * Draws a textured tile with x and y being true values not affected by {@link #tileSize tileSize}.
     * However, width and height are still affected by {@link #tileSize tileSize}.
     * @param texture a {@link Texture texture} to use.
     * Will clamp the size down to {@link Renderer#tileSize tileSize}
     * for width and height.
     * @param colour a {@link Colour colour} to use.
     * Will set a tint on the {@link Texture texture}.
     * @param x coordinate in pixels.
     * @param y coordinate in pixels.
     * @param layer z coordinate for layering. Will be ignored if {@link #setSortModeDeferred()
     * deferred} rendering is enabled.
     */
    public void drawFree(Texture texture, Colour colour, int x, int y, float layer)
    {
        drawSquare(texture.getID(), colour.hex(), x, y, layer);
    }

    /**
     * Draws a string of text as glyphs. x, y, width, and height values are affected 
     * by {@link #tileSize tileSize}. drawString will not word wrap.
     * Only supported escape character is '\n'.
     * @param text a string of text to draw.
     * @param colour a {@link Colour colour} to use.
     * Will set a tint on the {@link Texture texture}.
     * @param x coordinate in pixels.
     * @param y coordinate in pixels.
     * @param layer z coordinate for layering. Will be ignored if {@link #setSortModeDeferred()
     * deferred} rendering is enabled.
     */
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

    /**
     * Draws a string of text as glyphs. Only width, and height values are affected 
     * by {@link #tileSize tileSize}. drawString will not word wrap.
     * Only supported escape character is '\n'.
     * @param text a string of text to draw.
     * @param colour a {@link Colour colour} to use.
     * Will set a tint on the {@link Texture texture}.
     * @param x coordinate in pixels.
     * @param y coordinate in pixels.
     * @param layer z coordinate for layering. Will be ignored if {@link #setSortModeDeferred()
     * deferred} rendering is enabled.
     */
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

    /**
     * Fills four buffer slots with the given information. UV values will be
     * defaulted to 0 and 1.
     * @param id the OpenGL texture ID.
     * @param colour {@link Colour#hex() hexadecimal} value colour.
     * @param x coordinate in pixels.
     * @param y coordinate in pixels.
     * @param layer z coordinate for layering. Will be ignored if {@link #setSortModeDeferred()
     * deferred} rendering is enabled.
     */
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

    /**
     * Fills four buffer slots with the given information. Specialized for drawing glyphs
     * specifically.
     * @param glyph the glyph to draw.
     * @param slot the slot of the OpenGL texture ID given using {@link #getSlot(ArrayList, float) getSlot}.
     * @param colour {@link Colour#hex() hexadecimal} value colour.
     * @param x coordinate in pixels.
     * @param y coordinate in pixels.
     * @param layer z coordinate for layering. Will be ignored if {@link #setSortModeDeferred()
     * deferred} rendering is enabled.
     */
    private void drawGlyph(Glyph glyph, float slot, float colour, float x, float y, float layer)
    {
        flushIfNeeded(INDICES_SIZE);
        fillBuffer(slot, colour, x, y, layer, glyph.uv[0].x, glyph.uv[0].y);
        fillBuffer(slot, colour, x, y + tileSize, layer, glyph.uv[1].x, glyph.uv[1].y);
        fillBuffer(slot, colour, x + tileSize, y + tileSize, layer, glyph.uv[2].x, glyph.uv[2].y);
        fillBuffer(slot, colour, x + tileSize, y, layer, glyph.uv[3].x, glyph.uv[3].y);
        indexCount += INDICES_SIZE;
    }

    /**
     * Fills buffer with all data needed to properly fill a buffer slot.
     * @param slot the slot of the OpenGL texture ID given using {@link #getSlot(ArrayList, float) getSlot}.
     * @param colour {@link Colour#hex() hexadecimal} value colour.
     * @param x coordinate in pixels.
     * @param y coordinate in pixels.
     * @param layer z coordinate for layering. Will be ignored if {@link #setSortModeDeferred()
     * deferred} rendering is enabled.
     * @param u the U coordinate for the texture between 0 and 1.
     * @param v the V coordinate for the texture between 0 and 1.
     */
    private void fillBuffer(float slot, float colour, float x, float y, float layer, float u, float v)
    {
        buffer.put(x).put(y).put(layer);
        buffer.put(u).put(v);
        buffer.put(slot);
        buffer.put(colour);
    }

    /**
     * Ends the drawing process for the {@link SpriteBatch SpriteBatch}. An exception will be thrown if begin is either not called
     * or not called in the right order.
     * <p>
     * The order for drawing is:
     * <ul>
     * <li>{@link #begin() begin}
     * <li>end
     * <li>{@link #flush() flush}
     * </ul>
     * The listed order must be followed strictly.
     */
    public void end()
    {
        glUnmapBuffer(GL_ARRAY_BUFFER);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    /**
     * Flushes all currently submitted data from the draw functions onto the screen.
     * Will not be rendered until {@link Window#render() render} is called.
     * An exception will be thrown if begin is either not called
     * or not called in the right order.
     * <p>
     * The order for drawing is:
     * <ul>
     * <li>{@link #begin() begin}
     * <li>{@link #end() end}
     * <li>flush
     * </ul>
     * The listed order must be followed strictly.
     */
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
    
    /**
     * {@link #flush() flushes} all content from {@link #buffer buffer} if the
     * expected increase in indices goes over the {@link #RENDERER_INDICES_SIZE max indices count}.
     * @param expectedIndexCountIncrease the amount of indices the draw call is expected to draw.
     * Everything in {@link SpriteBatch SpriteBatch} is expected to be a square, so it should be the
     * {@link #INDICES_SIZE indices size} multiplied by the amount of submissions.
     */
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

    /**
     * Returns the x coordinate in pixels as if it were a tile.
     * @param x coordinate as a tile rather than a pixel
     */
    private float tilePosX(int x)
    {
        return x * tileSize;
    }

    /**
     * Returns the y coordinate in pixels as if it were a tile.
     * @param y coordinate as a tile rather than a pixel
     */
    private float tilePosY(int y)
    {
        return y * tileSize;
    }
}

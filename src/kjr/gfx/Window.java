package kjr.gfx;

import kjr.base.GameProgram;
import kjr.input.Input;
import kjr.math.Vec2;
import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFWVidMode;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glClearColor;

/**
 * Renders content onto the screen. The {@code Window} class creates an OpenGL
 * (via GL.createCapabilities) context
 * and uses this instance as the context.
 * Initializes glfw and the OpenGL library once.
 * <p>
 * When creating a {@code Window} object, it is important that the window dimensions are greater
 * than 0. If that requirement is not met, the rendering context will not be made.
 */
public class Window
{
    /**
     * Stops GLFW from being initialized more than once.
     */
    private static boolean IS_GLFW_INITTED = false;

    /**
     * Stops OpenGL from being initialized more than once.
     * <p>
     * LWJGL's OpenGL may be able to be initialized more than once, but because
     * there is only ever 1 context at a time, there isn't a need to 
     */
    private static boolean IS_OPENGL_INITTED = false;

    /**
     * GLFW callback function for setting window dimensions. Sets {@link kjr.gfx.Window#width width} and
     * {@link kjr.gfx.Window#height height} to the new dimension values,
     * and resizes the OpenGL viewport accordingly.
     * @param window the glfw window
     * @param width the new width of the monitor in pixels
     * @param height the new height of the monitor in pixels
     */
    private void windowResizeCallback(long window, int width, int height)
    {
        this.width = width;
        this.height = height;
        glViewport(0, 0, width, height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        game.windowResize();
    }

    /**
     * GLFW callback function for setting input keys. Sets a key
     * in {@link kjr.input.Input Input} to the key's state (pressed or not).
     * @param window the glfw window
     * @param key the key being set
     * @param scancode the code for the key to query
     * @param action what is happening to the key
     * @param mods the key's modifiers
     */
    private void keyCallback(long window, int key, int scancode, int action, int mods)
    {
        Input.setKey(key, action != GLFW_RELEASE);
    }

    /**
     * GLFW callback function for setting input buttons. Sets a button
     * in {@link kjr.input.Input Input} to the button's state (pressed or not).
     * @param window the glfw window
     * @param button the button being set
     * @param action what is happening to the button
     * @param mods the button's modifiers
     */
    private void buttonCallback(long window, int button, int action, int mods)
    {
        Input.setButton(button, action != GLFW_RELEASE);
    }

    private void scrollWheelCallback(long window, double xOffset, double yOffset)
    {
        Input.setScrollWheel(xOffset, yOffset);
    }

    /**
     * GLFW callback function for setting the mouse positions. Calls {@link kjr.input.Input Input's}
     * {@link kjr.input.Input#setX() setX} and {@link kjr.input.Input#setY() setY}
     * according to the x and y arguments.
     * @param window the glfw window
     * @param x the mouse x coordinate
     * @param y the mouse y coordinate
     */
    private void cursorPositionCallback(long window, double x, double y)
    {
        Input.setX((float)x);
        Input.setY((float)y);
    }


    /**
     * The {@link kjr.base.GameProgram GameProgram} instance to be used for
     * callback functions such as {@link kjr.base.GameProgram#windowResize() GameProgram.windowResize}
     */
    private GameProgram game;

    /**
     * The pointer to the GLFW window in C which we use
     * to access our created window outside of Java in Java.
     * Set to 0 by default as 0 represents NULL.
     */
    private long glfw_window = 0;

    /**
     * Title of the {@link kjr.gfx.Window Window}.
     */
    private String title = null;

    /**
     * Width of the {@link kjr.gfx.Window Window} in pixels.
     */
    private int width = 0;

    /**
     * Width of the {@link kjr.gfx.Window Window} in pixels.
     */
    private int height = 0;

    /**
     * Whether or not the {@link kjr.gfx.Window Window} uses vsync and waits for 1 v-blank.
     * If using vsync, then the window waits for the next vblank for swapping
     * front and back buffers. If not using vsync, then the window does not
     * wait for vblanks for swapping front and back buffers.
     * <p>
     * 0 = not using vsync, swap buffers when next able.
     * 1 = using vsync, swap buffers only as needed.>
     */
    private int vsync = 1;

    /**
     * Constructs a {@link kjr.gfx.Window Window}.
     * Window construction only initializes GLFW/OpenGL once,
     * meaning the context will only be at the first window created.
     * @param title the title of the window
     * @param width the width of the window in pixels
     * @param height the height of the window in pixels
     * @param game the game program used for user defined callback functions
     * @param limit_framerate whether or not to limit the framerate
     */
    public Window(String title, int width, int height, GameProgram game, boolean limit_framerate)
    {
        this.title = title;
        this.width = width;
        this.height = height;
        this.game = game;
        this.vsync = (limit_framerate) ? 1 : 0;
        initWindow();
    }

    /**
     * Initialization step for creating the {@link kjr.gfx.Window Window}.
     */
    private void initWindow()
    {
        // do not allow initialization to happen
        // more than once
        if(!IS_GLFW_INITTED)
        {
            // glfwInit() returns false if initialization fails
            if(!glfwInit())
            {
                throw new IllegalStateException("GLFW could not be successfully initialized.");
            }

            IS_GLFW_INITTED = true;
        }

        else
        {
            // this may change, we may not want to raise an exception for trying to create
            // more than one window
            throw new IllegalStateException("A window already exists in this process.");
        }
        
        // ensure all window hints are defaulted
        glfwDefaultWindowHints();

        // because assets may want to be loaded before the window pops up
        // we hide it by default and force the user to use Window.show()
        // so they can execute any initialization code before the window pops up
        // if the loading process takes a long time, the window will show white
        // and wont respond, which isn't very attractive to look at
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        glfw_window = glfwCreateWindow(width, height, title, 0, 0);

        // if the window is still NULL (0)
        // then the window wasn't created successfully
        if(glfw_window == 0)
        {
            throw new RuntimeException("Window is null.");
        }

        // set this window to run on this thread
        glfwMakeContextCurrent(glfw_window);

        // I'm guessing that this makes the context
        // for OpenGL tied to this window (or rather the glfw_window)
        if(!IS_OPENGL_INITTED)
        {
            GL.createCapabilities();
            IS_OPENGL_INITTED = true;
        }

        else
        {
            // this may change, we may not want to raise an exception for trying to create
            // more than one OpenGL context
            throw new IllegalStateException("OpenGL already initialized.");
        }
        
        // if all the initialization succeeds, set the callback
        // functions
        glfwSetKeyCallback(glfw_window, this::keyCallback);
        glfwSetMouseButtonCallback(glfw_window, this::buttonCallback);
        glfwSetCursorPosCallback(glfw_window, this::cursorPositionCallback);
        glfwSetWindowSizeCallback(glfw_window, this::windowResizeCallback);

        glfwSetScrollCallback(glfw_window, this::scrollWheelCallback);

        // whether or not to use vsync
        glfwSwapInterval(vsync);

        // swap the buffers for the first frame
        glfwSwapBuffers(glfw_window);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Removes all the stuff rendered previously from the {@link kjr.gfx.Window Window}.
     * Clears both the OpenGL colour buffer bits and
     * depth buffer bits. It's recommended to clear
     * everything previously rendered, making the window
     * a clean slate.
     * @param r the red value between 0 and 1
     * @param g the green value between 0 and 1
     * @param b the blue value between 0 and 1
     * @param a the alpha value between 0 and 1
     */
    public void clear(float r, float g, float b, float a)
    {
        glClearColor(r, g, b, a);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Polls for events/callbacks and updates the {@link kjr.input.Input Input} buttons, keys, and scrollwheel.
     */
    public void update()
    {
        Input.update();
        int error = glGetError();
        if(error != GL_NO_ERROR)
        {
            System.out.println("OpenGL error " + error);
        }
        glfwPollEvents();
    }

    /**
     * Renders all submitted data. Swaps the front and back buffers of the {@link kjr.gfx.Window Window}.
     */
    public void render()
    {
        glfwSwapBuffers(glfw_window);
    }

    /**
     * Minimizes the {@link kjr.gfx.Window Window} if it isn't already minimized.
     */
    public void hide()
    {
        glfwHideWindow(glfw_window);
    }

    /**
     * Shows the {@link kjr.gfx.Window Window} if it isn't already showing.
     */
    public void show()
    {
        glfwShowWindow(glfw_window);
    }

    /**
     * Deletes all of {@link kjr.gfx.Window Window's} native resources.
     * Calling {@link kjr.gfx.Window#delete() delete} will not delete the
     * {@link kjr.gfx.Window Window} from Java, but it will render it unusable.
     * Should only call once the {@link kjr.base.GameProgram GameProgram} is closing.
     */
    public void delete()
    {
        glfwSetWindowShouldClose(glfw_window, true);
        // frees all resorces allocated by glfw
        glfwTerminate();
        glfwDestroyWindow(glfw_window);
    }

    /**
     * Returns whether or not {@link kjr.gfx.Window Window} is still active.
     */
    public boolean running()
    {
        return !glfwWindowShouldClose(glfw_window);
    }

    /**
     * Sets the {@link kjr.gfx.Window Window} title to a new title.
     */
    public void setTitle(String newTitle)
    {
        title = newTitle;
        glfwSetWindowTitle(glfw_window, newTitle);
    }


    /**
     * Returns the monitor's width in pixels.
     */
    public int getScreenWidth()
    {
        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        return mode.width();
    }

    /**
     * Returns the primary monitor height in pixels.
     */
    public int getScreenHeight()
    {
        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        return mode.height();
    }

    /**
     * Returns the primary monitor resolution in pixels. Creates a Vec2
     * object when called.
     */
    public Vec2 getScreenResolution()
    {
        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        return new Vec2(mode.width(), mode.height());
    }

    /**
     * Returns the window width in pixels.
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Returns the window height in pixels.
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Returns the window dimensions in pixels.
     */
    public Vec2 getDimensions()
    {
        return new Vec2(width, height);
    }

    /**
     * Returns elapsed time in milliseconds
     */
    public double getTime()
    {
        return glfwGetTime() / 100000000;
    }

    /**
     * Closes the {@link kjr.gfx.Window Window} by hinting to GLFW that the
     * window should close as requested by the user.
     */
    public void close()
    {
        glfwSetWindowShouldClose(glfw_window, true);
    }
}
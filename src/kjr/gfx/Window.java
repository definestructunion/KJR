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
 * <pre>
 * Brief: Renders content onto the screen.
 * 
 * Layman:
 * 
 * This class sets up GLFW (the window) and OpenGL to work
 * with this object. Any rendering will be displayed on this
 * screen.
 * 
 * ////
 * 
 * Non-Layman:
 * 
 * This class creates an OpenGL context and uses this instance
 * as the context.
 * 
 * Initializes glfw and the OpenGL library once
 * 
 * Wrapper class for GLFW
 * 
 * ////
 * 
 * Contains:
 * - Title as String - title of the window
 * - Width as int - width of screen in pixels
 * - Height as int - height of screen in pixels
 * - GLFW Window as long - the pointer to the object in C
 * 
 * ////
 * </pre>
 */
public class Window
{
    /**
     * <pre>
     * Brief: Stops GLFW from being initialized more than once.
     * 
     * Layman:
     * 
     * GLFW doesn't like being initialized multiple times.
     * 
     * ////
     * 
     * Non-Layman:
     * 
     * While GLFW can be successfully initialized multiple times,
     * GLFW doesn't support multiple instances of itself in a single
     * process.
     * 
     * ////
     * </pre>
     */
    private static boolean IS_GLFW_INITTED = false;

    /**
     * <pre>
     * Brief: Stops OpenGL from being initialized more than once.
     * 
     * Note: I assume that LWJGL's initializing is similar to GLEW,
     * where it doesn't like being initialized more than once.
     * 
     * Layman:
     * 
     * OpenGL doesn't like being initialized more than once.
     * 
     * ////
     * 
     * Non-Layman:
     * 
     * LWJGL's OpenGL may be able to be initialized more than once, but because
     * there is only ever 1 context at a time, there isn't a need to 
     * 
     * ////
     */
    private static boolean IS_OPENGL_INITTED = false;

    /**
     * <pre>
     * Brief: GLFW callback function for setting window dimensions.
     * </pre>
     * @param window - glfw window
     * @param width - new width of the monitor in pixels
     * @param height - new height of the monitor in pixels
     */
    private void windowResizeCallback(long window, int width, int height)
    {
        this.width = width;
        this.height = height;
        glViewport(0, 0, width, height);
        game.windowResize();
    }

    /**
     * <pre>
     * Brief: GLFW callback function for setting input keys.
     * </pre>
     * @param window - glfw window
     * @param key - key being set
     * @param scancode - code for the key to query
     * @param action - what is happening to the key
     * @param mods - key's modifiers
     */
    private void keyCallback(long window, int key, int scancode, int action, int mods)
    {
        Input.setKey(key, action != GLFW_RELEASE);
    }

    /**
     * <pre>
     * Brief: GLFW callback function for setting input buttons.
     * </pre>
     * @param window - glfw window
     * @param button - button being set
     * @param action - what is happening to the button
     * @param mods - button's modifiers
     */
    private void buttonCallback(long window, int button, int action, int mods)
    {
        Input.setButton(button, action != GLFW_RELEASE);
    }

    /**
     * <pre>
     * Brief: GLFW callback function for setting the mouse positions.
     * </pre>
     * @param window
     * @param x
     * @param y
     */
    private void cursorPositionCallback(long window, double x, double y)
    {
        Input.setX((float)x);
        Input.setY((float)y);
    }


    /**
     * <pre>
     * Brief: the current game running used for user callbacks.
     * 
     * Layman:
     * 
     * Since the game window is tied to the current game being used
     * we can use it to allow the user to make their own callback
     * functions if they wish to make them.
     * 
     * ////
     * </pre>
     */
    private GameProgram game;

    /**
     * <pre>
     * Brief: The actual window being rendererd to.
     * 
     * Layman:
     * 
     * This class only allows functionality to the window.
     * This class doesn't actually manage the window itself, but
     * exists to add flexibility to the window.
     * 
     * ////
     * 
     * Non-Layman:
     * 
     * The pointer to the GLFW window in C which we use
     * to access our created window outside of Java in Java.
     * Set to 0 by default as 0 represents NULL.
     * 
     * ////
     * </pre>
     */
    private long glfw_window = 0;

    /**
     * <pre>
     * Brief: The title of the window.
     * </pre>
     */
    private String title = null;

    /**
     * <pre>
     * Brief: The width of the window in pixels.
     * 
     * Non-Layman:
     * 
     * Value is set to 0 by default as GLFW asserts [width > 0]
     * halting the program if [!(width > 0)]
     * 
     * ////
     * </pre>
     */
    private int width = 0;

    /**
     * <pre>
     * Brief: The height of the window in pixels.
     * 
     * Non-Layman:
     * 
     * Value is set to 0 by default as GLFW asserts [height > 0]
     * halting the program if [!(height > 0)]
     * 
     * ////
     * </pre>
     */
    private int height = 0;

    /**
     * <pre>
     * Brief: Whether or not the window uses vsync and
     *       waits for 1 v-blank
     * 
     * Note: 0 = not using vsync
     * Note: 1 = using vsync
     * 
     * Layman:
     * 
     * If using vsync (vsync = 1), then it means
     * that it renders only when the monitor is
     * ready to display the information. Recommended
     * to use vsync (= 1).
     * 
     * ////
     * 
     * Non-Layman:
     * 
     * If using vsync, then the window waits for the next vblank for swapping
     * front and back buffers. If not using vsync, then the window does not
     * wait for vblanks for swapping front and back buffers.
     * 
     * ////
     * </pre>
     */
    private int vsync = 1;

    /**
     * <pre>
     * Brief: Constructs a window.
     * 
     * Note: Window construction only initializes GLFW/OpenGL once,
     * meaning the context will only be at the first window created.
     * 
     * Note: For more information, read the class javadoc.
     * </pre> 
     * @param title - title of the window
     * @param width - width of the window in pixels
     * @param height - height of the window in pixels
     * @param game - game program used for user defined callback functions
     * @param limit_framerate - whether or not to limit the framerate
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
     * Brief: Initialization step for creating the window
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

        // whether or not to use vsync
        glfwSwapInterval(vsync);

        // swap the buffers for the first frame
        glfwSwapBuffers(glfw_window);

        glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * <pre>
     * Brief: Clears the screen.
     * 
     * Layman:
     * 
     * Removes all the stuff rendered previously from the screen.
     * 
     * ////
     * 
     * Non-Layman:
     * 
     * Clears both the OpenGL color buffer bits and
     * depth buffer bits. It's recommended to clear
     * everything previously rendered, making the window
     * a clean slate.
     * 
     * ////
     * </pre>
     * @param r - red value between 0 and 1
     * @param g - green value between 0 and 1
     * @param b - blue value between 0 and 1
     * @param a - alpha value between 0 and 1
     */
    public void clear(float r, float g, float b, float a)
    {
        glClearColor(r, g, b, a);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * <pre>
     * Brief: Updates all events such as input and callbacks.
     * </pre>
     */
    public void update()
    {
        Input.updateKeys();
        int error = glGetError();
        if(error != GL_NO_ERROR)
        {
            System.out.println("OpenGL error " + error);
        }
        glfwPollEvents();
    }

    /**
     * <pre>
     * Brief: Renders all submitted data.
     * 
     * Layman:
     * 
     * Anything that was told to render onto the screen
     * will be rendered onto the screen. If nothing shows up,
     * something is messed up.
     * 
     * ////
     * 
     * Non-Layman:
     * 
     * Swaps the front and back buffers of the GLFW window.
     * 
     * ////
     * </pre>
     */
    public void render()
    {
        glfwSwapBuffers(glfw_window);
    }

    /**
     * <pre>
     * Brief: Hides the window (minimizes).
     * </pre>
     */
    public void hide()
    {
        glfwHideWindow(glfw_window);
    }

    /**
     * <pre>
     * Brief: Shows the window if it's hidden
     *       otherwise, doesn't do anything.
     * </pre>
     */
    public void show()
    {
        glfwShowWindow(glfw_window);
    }

    /**
     * <pre>
     * Brief: Deletes all native memory.
     * 
     * Warning: Will cause memory leak if delete
     * is not called once GC collects the window.
     * 
     * Layman:
     * 
     * There is memory that the java GC can not collect.
     * You need to explicitely call delete() once
     * done with the window.
     * 
     * ////
     * 
     * Non-Layman:
     * 
     * Since the GLFW window is bound to native resources (C),
     * java GC does not know about it and therefor cannot collect it.
     * Explicitely call delete on a pointer object once
     * done with the resource.
     * 
     * ////
     * </pre>
     */
    public void delete()
    {
        glfwSetWindowShouldClose(glfw_window, true);
        // frees all resorces allocated by glfw
        glfwTerminate();
        glfwDestroyWindow(glfw_window);
    }

    /**
     * <pre>
     * Brief: Returns true if the window is still active.
     * </pre>
     */
    public boolean running()
    {
        return !glfwWindowShouldClose(glfw_window);
    }

    /**
     * <pre>
     * Brief: Changes the title.
     * </pre>
     * @param new_title - new title to change to
     */
    public void setTitle(String new_title)
    {
        title = new_title;
        glfwSetWindowTitle(glfw_window, new_title);
    }


    /**
     * <pre>
     * Brief: Returns the primary monitor width in pixels.
     * </pre>
     * @return int - monitor width
     */
    public int getScreenWidth()
    {
        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        return mode.width();
    }

    /**
     * <pre>
     * Brief: Returns the primary monitor height in pixels.
     * </pre>
     * @return int - monitor height
     */
    public int getScreenHeight()
    {
        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        return mode.height();
    }

    /**
     * <pre>
     * Brief: Returns the primary monitor resolution in pixels.
     * </pre>
     * @return Vec2 - monitor resolution
     */
    public Vec2 getScreenResolution()
    {
        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        return new Vec2(mode.width(), mode.height());
    }

    /**
     * <pre>
     * Brief: Returns the window width in pixels.
     * </pre>
     * @return int - window width
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * <pre>
     * Brief: Returns the window height in pixels.
     * </pre>
     * @return int - window height
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * <pre>
     * Brief: Returns the window dimensions in pixels.
     * </pre>
     * @return Vec2 - window dimensions
     */
    public Vec2 getDimensions()
    {
        return new Vec2(width, height);
    }
}
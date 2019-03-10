package kjr.input;

import kjr.math.Vec2;

/**
 * Contains information about {@link kjr.input.Keys Keys}, {@link kjr.input.Buttons Buttons},
 * Mouse position, and scroll wheel information. This information is set by GLFW callbacks. While
 * user can set this information manually, it's not recommended as the user should not force state.
 * <p>
 * Input allows the user to query for various states of the application. They can check for specific keys
 * and buttons, whether they are held down or pressed. They can check for the mouse position and check for
 * movement of the scroll wheel.
 * <p>
 * To update this information, it's imperative that either {@link kjr.gfx.Window#update() Window.update} or
 * {@link kjr.input.Input#update() Input.update} are called. Without updating Input, the state will be locked
 * and prone to inaccuracy.
 */
public final class Input
{
    /**
     * The amount of keys to go into {@link kjr.input.KeyState KeyState} keys.
     */
    public static final int MAX_KEYS    = 1024;

    /**
     * The amount of buttons to go into {@link kjr.input.KeyState KeyState} buttons.
     */
    public static final int MAX_BUTTONS = 32;

    /**
     * The {@link kjr.input.KeyState KeyState} holding the key state
     * for GLFW callback functions.
     * <p>
     * This is useful for the previous KeyState, which relies
     * on both glfw and current.
     */
    private static KeyState glfw = new KeyState();

    /**
     * The currently fired keys. This {@link kjr.input.KeyState KeyState} is useful
     * for getting the current frame's key state.
     */
    private static KeyState current = new KeyState();

    /**
     * The previously fired keys. Values are set to reflect a key/button to only fire once
     * when held down, rather than continuously.
     */
    private static KeyState previous = new KeyState();

    /**
     * X position of the cursor.
     * Note that (0, 0) is located at the top left
     */
    private static float x;

    /**
     * Y position of the cursor.
     * Note that (0, 0) is located at the top left
     */
    private static float y;

    /**
     * Position of the cursor a vector. Created in {@link kjr.input.Input Input} so that
     * calling {@link kjr.input.Input#getMousePosition() getMousePosition} does not allocate
     * resources to be collected soon after.
     */
    private static Vec2 mousePosition = new Vec2(0, 0);

    /**
     * Scroll position on the X axis. Most mice do not support X axis scrolling (right, left).
     */
    private static double scrollX = 0;

    /**
     * Scroll position on the Y axis. Most mice do support Y axis Scrolling (up, down).
     */
    private static double scrollY = 0;

    /**
     * GLFW callback on scrolling does not reset the values of {@link kjr.input.Input#scrollX scrollX}
     * and {@link kjr.input.Input#scrollY scrollY}. Therefore, it's mimicked by scrollCount, which only returns
     * {@link kjr.input.Input#scrollX scrollX} and {@link kjr.input.Input#scrollY scrollY} values if scrollCount
     * is greater than 0.
     */
    private static int scrollCount = 0;

    /**
     * Updates the {@link kjr.input.KeyState KeyState} and {@link kjr.input.Input#scrollCount scrollCount}
     * to reflect the new frame.
     */
    public static void update()
    {
        for(int i = 0; i < MAX_KEYS; ++i)
        {
            // set the key to true if:
            // it was not fired last frame
            // if it's being fired this frame
            previous.keys[i] = glfw.keys[i] && !current.keys[i];
        }

        // copy the value of glfw keys to current keys
        // as they are set each frame
        System.arraycopy(glfw.keys, 0, current.keys, 0, MAX_KEYS);

        for(int i = 0; i < MAX_BUTTONS; ++i)
        {
            // set the button to true if:
            // it was not fired last frame
            // if it's being fired this frame
            previous.buttons[i] = glfw.buttons[i] && !current.buttons[i];
        }

        // copy the value of glfw buttons to current buttons
        // as they are set each frame
        System.arraycopy(glfw.buttons, 0, current.buttons, 0, MAX_BUTTONS);

        if(scrollCount > 0)
            --scrollCount;
    }

    /**
     * Returns whether or not the specified key is down.
     */
    public static boolean keyDown(Keys key)
    {
        return current.keys[key.value()];
    }

    /**
     * Returns true if the key is pressed down.
     * The difference between keyPressed and
     * {@link kjr.input.Input#keyDown(Keys) keyDown} is that
     * keyPressed is only fired once when held down
     * and must be released to be able to be fired again.
     * @param key the key to check
     */
    public static boolean keyPressed(Keys key)
    {
        return previous.keys[key.value()];
    }

    /**
     * Returns whether or not the button is down.
     * @param button the button to check
     */
    public static boolean buttonDown(Buttons button)
    {
        return current.buttons[button.value()];
    }

    /**
     * Returns whether or not the button is pressed down.
     * The difference between buttonPressed and
     * {@link kjr.input.Input#buttonDown(Buttons) buttonDown} is that
     * buttonPressed is only fired once when held down
     * and must be released to be able to be fired again.
     * @param button the button to check
     */
    public static boolean buttonPressed(Buttons button)
    {
        return previous.buttons[button.value()];
    }

    /**
     * Sets the corresponding key to val.
     * <p>
     * This should not be used by user, this is
     * designed for GLFW callbacks
     * @param key the key being set
     * @param val the value of the key (true, false)
     */
    public static void setKey(int key, boolean val)
    {
        glfw.keys[key] = val;
    }

    /**
     * Sets the corresponding key to val.
     * <p>
     * This should not be used by user, this is
     * designed for GLFW callbacks
     * @param button the button being set
     * @param val the value of the button (true, false)
     */
    public static void setButton(int button, boolean val)
    {
        glfw.buttons[button] = val;
    }

    /**
     * Returns the X position of the mouse.
     */
    public static double getX()
    {
        return x;
    }

    /**
     * Returns the Y position of the mouse.
     */
    public static double getY()
    {
        return y;
    }

    /**
     * Sets the mouse X position.
     * <p>
     * This should not be used by user, this is
     * designed for GLFW callbacks.
     * @param val the new X value
     */
    public static void setX(float val) { x = val; }

    /**
     * Sets the mouse Y position.
     * <p>
     * This should not be used by user, this is
     * designed for GLFW callbacks.
     * @param val the new Y value
     */
    public static void setY(float val) { y = val; }

    public static Vec2 getMousePosition()
    {
        mousePosition.x = x;
        mousePosition.y = y;
        return mousePosition;
    }

    /**
     * Sets the new scroll wheel values.
     * <p>
     * This should not be used by user, this is
     * designed for GLFW callbacks.
     * @param xOffset the new X value of scrollX
     * @param yOffset the new Y value of scrollY
     */
    public static void setScrollWheel(double xOffset, double yOffset)
    {
        scrollX = xOffset;
        scrollY = yOffset;
        // I don't know why this works, but it works
        // perfectly, don't touch
        scrollCount += 2;
    }

    /**
     * Returns the {@link kjr.input.Input#scrollX scrollX} value only if
     * {@link kjr.input.Input#scrollCount} is greater than 0. If
     * {@link kjr.input.Input#scrollCount} is equal to 0, getScrollY returns
     * 0.0.
     */
    public static double getScrollX()
    {
        if(scrollCount > 0)
            return scrollX;
        return 0.0;
    }

    /**
     * Returns the {@link kjr.input.Input#scrollY scrollY} value only if
     * {@link kjr.input.Input#scrollCount} is greater than 0. If
     * {@link kjr.input.Input#scrollCount} is equal to 0, getScrollX returns
     * 0.0.
     */
    public static double getScrollY()
    {
        if(scrollCount > 0) {
            return scrollY;
        }
        return 0.0;
    }
}

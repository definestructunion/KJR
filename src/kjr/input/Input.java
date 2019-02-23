package kjr.input;

import kjr.math.Vec2;

/**
 * <pre>
 * Brief: The keystate of the application.
 * 
 * Layman:
 * 
 * All key related activities are kept here, such as keys being
 * pressed or not
 * 
 * Contains:
 * Max Keys as final int - amount of keys in the key array
 * Max Buttons as final int - amount of buttons in the button array
 * GLFW as KeyState - keys set by GLFW on callback
 * Current as KeyState - keys that are being fired this frame
 * Previous as KeyState - keys that are being fired this frame
 *                        but only remain true for one frame
 *                        until released
 * </pre>
 */
public final class Input
{
    /**
     * <pre>
     * Brief: The amount of keys.
     * </pre>
     */
    public static final int MAX_KEYS    = 1024;

    /**
     * <pre>
     * Brief: The amount of buttons.
     * </pre>
     */
    public static final int MAX_BUTTONS = 32;

    /**
     * <pre>
     * Brief: The KeyState holding the GLFW keys
     * 
     * Note: This is useful for the previous KeyState, which relies
     * on both glfw and current.
     * </pre>
     */
    private static KeyState glfw = new KeyState();

    /**
     * <pre>
     * Brief: The currently fired keys.
     * </pre>
     */
    private static KeyState current = new KeyState();

    /**
     * <pre>
     * Brief: Keys that are only fired once when held down
     * allowed to be fired again once released.
     * </pre>
     */
    private static KeyState previous = new KeyState();

    /**
     * <pre>
     * Brief: Updates the current and previous KeyStates.
     * 
     * Note: Can't be used to set GLFW keys
     * </pre>
     */
    public static void updateKeys()
    {
        for(int i = 0; i < MAX_KEYS; ++i)
        {
            // set the key to true if:
            // it was not fired last frame
            // if it's being fired this frame
            previous.keys[i] = glfw.keys[i] && !current.keys[i];
            if(previous.keys[i])
                System.out.println("true" + i);
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
    }

    /**
     * <pre>
     * Brief: Returns whether or not the specified key is down.
     * </pre>
     * @param key - key to check
     * @return - true if key is fired
     */
    public static boolean keyDown(Keys key)
    {
        return current.keys[key.value()];
    }

    /**
     * <pre>
     * Brief: Returns true if the key is pressed down
     * 
     * Layman:
     * 
     * Holding the key down will only fire once,
     * meaning you have to release the key and press
     * it down again to fire the key.
     * 
     * Non-Layman:
     * 
     * Returns true if the key is pressed down,
     * the difference between this and keyDown is that
     * keyPressed is only fired once when held down
     * and must be released to be able to be fired again.
     * </pre>
     * @param key the key to check
     * @return true if the key is pressed
     */
    public static boolean keyPressed(Keys key)
    {
        return previous.keys[key.value()];
    }

    /**
     * <pre>
     * Brief: Returns true if the button is held down
     * </pre>
     * @param button - button to check
     * @return - true if the button is down
     */
    public static boolean buttonDown(Buttons button)
    {
        return current.buttons[button.value()];
    }

    /**
     * <pre>
     * Brief: Returns true if the button is pressed down.
     * 
     * Layman:
     * 
     * Holding the button down will only fire once,
     * meaning you have to release the button and press
     * it down again to fire the key.
     * 
     * Non-Layman:
     * 
     * Returns true if the button is pressed down,
     * the difference between this and buttonDown is that
     * buttonPressed is only fired once when held down
     * and must be released to be able to be fired again.
     * </pre>
     * @param button the button to check
     * @return true if the button is pressed
     */
    public static boolean buttonPressed(Buttons button)
    {
        return previous.buttons[button.value()];
    }

    /**
     * <pre>
     * Brief: Sets the according key to val.
     * 
     * Warning: This should not be used by a user, this is
     * designed for GLFW callbacks
     * </pre>
     * @param key - key being set
     * @param val - value of the button
     */
    public static void setKey(int key, boolean val)
    {
        glfw.keys[key] = val;
    }

    /**
     * <pre>
     * Brief: Sets the according button to val.
     * 
     * Warning: This should not be used by a user, this is
     * designed for GLFW callbacks
     * </pre>
     * @param button - button being set
     * @param val - value of the button
     */
    public static void setButton(int button, boolean val)
    {
        glfw.buttons[button] = val;
    }

    /**
     * <pre>
     * Brief: X position of the cursor
     * 
     * Note: (0, 0) is located at the top left
     * </pre>
     */
    private static float x;
    /**
     * <pre>
     * Brief: Y position of the cursor
     * 
     * Note: (0, 0) is located at the top left
     * </pre>
     */
    private static float y;

    /**
     * <pre>
     * Brief: Gets the X position of the mouse.
     * </pre>
     * @return double - mouse position's X
     */
    public static double getX()
    {
        return x;
    }

    /**
     * <pre>
     * Brief: Gets the Y position of the mouse.
     * </pre>
     * @return double - mouse position's Y
     */
    public static double getY()
    {
        return y;
    }

    /**
     * <pre>
     * Brief: Sets the mouse X position.
     * 
     * Warning: This should not be used by a user, this is
     * designed for GLFW callbacks.
     * </pre>
     * @param val - new X value
     */
    public static void setX(float val) { x = val; }

    /**
     * <pre>
     * Brief: Sets the mouse Y position.
     * 
     * Warning: This should not be used by a user, this is
     * designed for GLFW callbacks.
     * </pre>
     * @param val - new Y value
     */
    public static void setY(float val) { y = val; }

    private static Vec2 mousePosition = new Vec2(0, 0);
    public static Vec2 getMousePosition()
    {
        mousePosition.x = x;
        mousePosition.y = y;
        return mousePosition;
    }

    /**
     * If the key is down, it returns true and sets the key to false
     * @return
     */
    public static boolean keyDownSet(Keys key)
    {
        boolean isKeyDown = current.keys[key.value()];
        current.keys[key.value()] = false;
        return isKeyDown;
    }

    /**
     * If the key is pressed, it returns true and sets the key to false
     * @return
     */
    public static boolean keyPressedSet(Keys key)
    {
        boolean isKeyPressed = previous.keys[key.value()];
        previous.keys[key.value()] = false;
        return isKeyPressed;
    }

    /**
     * If the button is down, it returns true and sets the key to false
     * @return
     */
    public static boolean buttonDownSet(Buttons button)
    {
        boolean isButtonDown = current.buttons[button.value()];
        current.buttons[button.value()] = false;
        return isButtonDown;
    }

    /**
     * If the button is pressed, it returns true and sets the key to false
     * @return
     */
    public static boolean buttonPressedSet(Buttons button)
    {
        boolean isButtonPressed = previous.buttons[button.value()];
        previous.buttons[button.value()] = false;
        return isButtonPressed;
    }
}

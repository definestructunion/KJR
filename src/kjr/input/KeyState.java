package kjr.input;

import java.util.Arrays;

/**
 * <pre>
 * Brief: Holds information about key states.
 * 
 * Layman:
 * 
 * Holds information about whether or not a key
 * or button is being fired (pushed down).
 * 
 * ////
 * 
 * Non-Layman:
 * 
 * Holds generic information about an array of keys and their state.
 * This information is not tied to any particular time or state in the application.
 * Meaning KeyState works for any state of time, but must be recorded and defined as such.
 * Meaning if you want a key state for the last frame, you must treat the keys and buttons
 * as if they were keys and buttons from the last frame.
 * 
 * ////
 * 
 * Contains:
 * - Keys as boolean array - the states of all the keys
 * - Buttons as boolean array - the states of all the buttons
 * </pre>
 */
public class KeyState
{
    /**
     * <pre>
     * Brief: keys on the keyboard.
     * </pre>
     */
    boolean[] keys = new boolean[Input.MAX_KEYS];

    /**
     * <pre>
     * Brief: buttons on the mouse.
     * </pre>
     */
    boolean[] buttons = new boolean[Input.MAX_BUTTONS];

    /**
     * <pre>
     * Brief: Creates a state with every key and button
     *       set to false
     * </pre>
     */
    public KeyState()
    {
        // set all values to false
        Arrays.fill(keys, false);
        Arrays.fill(buttons, false);
    }
}

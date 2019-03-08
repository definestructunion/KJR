package kjr.input;

import java.util.Arrays;

/**
 * Holds information about key states.
 * <p>
 * Holds generic information about an array of keys and their state.
 * This information is not tied to any particular time or state in the application.
 * Meaning KeyState works for any state of time, but must be recorded and defined as such.
 * Meaning if you want a key state for the last frame, you must treat the keys and buttons
 * as if they were keys and buttons from the last frame.
 * 
 * Contains:
 * - Keys as boolean array - the states of all the keys
 * - Buttons as boolean array - the states of all the buttons
 */
public class KeyState
{
    /**
     * Keeps track of the keys in this {@link kjr.input.KeyState KeyState}.
     */
    boolean[] keys = new boolean[Input.MAX_KEYS];

    /**
     * Keeps track of the buttons in this {@link kjr.input.KeyState KeyState}.
     */
    boolean[] buttons = new boolean[Input.MAX_BUTTONS];

    /**
     * Creates a new key state with every {@link kjr.input.Keys Key} and {@link kjr.input.Buttons Button}
     * value set to false
     */
    public KeyState()
    {
        // set all values to false
        Arrays.fill(keys, false);
        Arrays.fill(buttons, false);
    }
}

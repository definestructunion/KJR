package kjr.input;

import java.util.Arrays;

/**
 * Holds information about key states.
 * <p>
 * Holds generic information about a {@link kjr.input.Keys Key} 
 * array and a {@link kjr.input.Buttons Button} array and their state.
 * This information is not tied to any particular time or state in the application.
 * Meaning KeyState works for any state of time, but must be recorded and defined as such.
 * Meaning if user want a key state for the last frame, user must treat the keys and buttons
 * as if they were keys and buttons from the last frame.
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

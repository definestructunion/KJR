package kjr.input;

/**
 * Represents buttons on a mouse. Button values should not be passed as ordinal(), as Buttons is not
 * created ordinally. Button values should be passed as value().
 */
public enum Buttons
{
    B1           (0),
    B2           (1),
    B3           (2),
    B4           (3),
    B5           (4),
    B6           (5),
    B7           (6),
    B8           (7),
    Last         (7), // B8
    Left         (0), // B1
    Right        (1), // B2
    Middle       (2), // B3
    LENGTH       (12);

    private final int number;
    
    Buttons(int number)
    {
        this.number = number;
    }

    /**
     * Returns the actual value of the key.
     */
    public int value()
    {
        return number;
    }
}

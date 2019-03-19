package kjr.input;

/**
 * Represents a key on a keyboard. Key values should not be passed as ordinal(), as Keys is not
 * created ordinally. Key values should be passed as value().
 */
public enum Keys{
    Unknown      (-1),
    Space        (32),
    Apostrophe   (39),
    Comma        (44),
    Minus        (45),
    Period       (46),
    Slash        (47),
    N0           (48),
    N1           (49),
    N2           (50),
    N3           (51),
    N4           (52),
    N5           (53),
    N6           (54),
    N7           (55),
    N8           (56),
    N9           (57),
    Semicolon    (59),
    Equal        (61),
    A            (65),
    B            (66),
    C            (67),
    D            (68),
    E            (69),
    F            (70),
    G            (71),
    H            (72),
    I            (73),
    J            (74),
    K            (75),
    L            (76),
    M            (77),
    N            (78),
    O            (79),
    P            (80),
    Q            (81),
    R            (82),
    S            (83),
    T            (84),
    U            (85),
    V            (86),
    W            (87),
    X            (88),
    Y            (89),
    Z            (90),
    BracketLeft  (91),
    Backslash    (92),
    BracketRight (93),
    Grave        (96),
    World1       (161),
    World2       (162),

    Escape       (256),
    Enter        (257),
    Tab          (258),
    Backspace    (259),
    Insert       (260),
    Delete       (261),
    Right        (262),
    Left         (263),
    Down         (264),
    Up           (265),
    PageUp       (266),
    PageDown     (267),
    Home         (268),
    End          (269),
    CapsLock     (280),
    ScrollLock   (281),
    NumLock      (282),
    PrintScreen  (283),
    Pause        (284),
    F1           (290),
    F2           (291),
    F3           (292),
    F4           (293),
    F5           (294),
    F6           (295),
    F7           (296),
    F8           (297),
    F9           (298),
    F10          (299),
    F11          (300),
    F12          (301),
    F13          (302),
    F14          (303),
    F15          (304),
    F16          (305),
    F17          (306),
    F18          (307),
    F19          (308),
    F20          (309),
    F21          (310),
    F22          (311),
    F23          (312),
    F24          (313),
    F25          (314),
    KP0          (320),
    KP1          (321),
    KP2          (322),
    KP3          (323),
    KP4          (324),
    KP5          (325),
    KP6          (326),
    KP7          (327),
    KP8          (328),
    KP9          (329),
    KPDec        (330),
    KPDiv        (331),
    KPMult       (332),
    KPSub        (333),
    KPAdd        (334),
    KPEnter      (335),
    KPEqual      (336),
    LeftShift    (340),
    LeftCtrl     (341),
    LeftAlt      (342),
    LeftSuper    (343),
    RightShift   (344),
    RightCtrl    (345),
    RightAlt     (346),
    RightSuper   (347),
    Menu         (348),
    LENGTH       (119);

    private final int number;
    private final char noShiftChar;
    private final char shiftChar;

    // noShiftChar = ''; shiftChar = ''; break;

    Keys(int number)
    {
        this.number = number;
        switch(number)
        {
            case -1: noShiftChar = '\u0000'; shiftChar = '\u0000'; break;
            case 32: noShiftChar = ' '; shiftChar = ' '; break;
            case 39: noShiftChar = '\''; shiftChar = '\"'; break;
            case 44: noShiftChar = ','; shiftChar = '<'; break;
            case 45: noShiftChar = '-'; shiftChar = '_'; break;
            case 46: noShiftChar = '.'; shiftChar = '>'; break;
            case 47: noShiftChar = '/'; shiftChar = '?'; break;
            case 48: noShiftChar = '0'; shiftChar = ')'; break;
            case 49: noShiftChar = '1'; shiftChar = '!'; break;
            case 50: noShiftChar = '2'; shiftChar = '@'; break;
            case 51: noShiftChar = '3'; shiftChar = '#'; break;
            case 52: noShiftChar = '4'; shiftChar = '$'; break;
            case 53: noShiftChar = '5'; shiftChar = '%'; break;
            case 54: noShiftChar = '6'; shiftChar = '^'; break;
            case 55: noShiftChar = '7'; shiftChar = '&'; break;
            case 56: noShiftChar = '8'; shiftChar = '*'; break;
            case 57: noShiftChar = '9'; shiftChar = '('; break;
            case 59: noShiftChar = ';'; shiftChar = ':'; break;
            case 61: noShiftChar = '='; shiftChar = '+'; break;
            case 65: noShiftChar = 'a'; shiftChar = 'A'; break;
            case 66: noShiftChar = 'b'; shiftChar = 'B'; break;
            case 67: noShiftChar = 'c'; shiftChar = 'C'; break;
            case 68: noShiftChar = 'd'; shiftChar = 'D'; break;
            case 69: noShiftChar = 'e'; shiftChar = 'E'; break;
            case 70: noShiftChar = 'f'; shiftChar = 'F'; break;
            case 71: noShiftChar = 'g'; shiftChar = 'G'; break;
            case 72: noShiftChar = 'h'; shiftChar = 'H'; break;
            case 73: noShiftChar = 'i'; shiftChar = 'I'; break;
            case 74: noShiftChar = 'j'; shiftChar = 'J'; break;
            case 75: noShiftChar = 'k'; shiftChar = 'K'; break;
            case 76: noShiftChar = 'l'; shiftChar = 'L'; break;
            case 77: noShiftChar = 'm'; shiftChar = 'M'; break;
            case 78: noShiftChar = 'n'; shiftChar = 'N'; break;
            case 79: noShiftChar = 'o'; shiftChar = 'O'; break;
            case 80: noShiftChar = 'p'; shiftChar = 'P'; break;
            case 81: noShiftChar = 'q'; shiftChar = 'Q'; break;
            case 82: noShiftChar = 'r'; shiftChar = 'R'; break;
            case 83: noShiftChar = 's'; shiftChar = 'S'; break;
            case 84: noShiftChar = 't'; shiftChar = 'T'; break;
            case 85: noShiftChar = 'u'; shiftChar = 'U'; break;
            case 86: noShiftChar = 'v'; shiftChar = 'V'; break;
            case 87: noShiftChar = 'w'; shiftChar = 'W'; break;
            case 88: noShiftChar = 'x'; shiftChar = 'X'; break;
            case 89: noShiftChar = 'y'; shiftChar = 'Y'; break;
            case 90: noShiftChar = 'z'; shiftChar = 'Z'; break;
            case 91: noShiftChar = '['; shiftChar = '{'; break;
            case 92: noShiftChar = '\\'; shiftChar = '|'; break;
            case 93: noShiftChar = ']'; shiftChar = '}'; break;
            case 96: noShiftChar = '`'; shiftChar = '~'; break;
            case 257: noShiftChar = '\n'; shiftChar = '\n'; break;
            //case 258: noShiftChar = '\b'; shiftChar = '\b'; break; // will almost certainly not work as expected of a backspace
            case 320: noShiftChar = '0'; shiftChar = '0'; break;
            case 321: noShiftChar = '1'; shiftChar = '1'; break;
            case 322: noShiftChar = '2'; shiftChar = '2'; break;
            case 323: noShiftChar = '3'; shiftChar = '3'; break;
            case 324: noShiftChar = '4'; shiftChar = '4'; break;
            case 325: noShiftChar = '5'; shiftChar = '5'; break;
            case 326: noShiftChar = '6'; shiftChar = '6'; break;
            case 327: noShiftChar = '7'; shiftChar = '7'; break;
            case 328: noShiftChar = '8'; shiftChar = '8'; break;
            case 329: noShiftChar = '9'; shiftChar = '9'; break;
            case 330: noShiftChar = '0'; shiftChar = '0'; break;
            default: noShiftChar = '\u0000'; shiftChar = '\u0000'; break;
        }
    }

    /**
     * Returns the actual value of the key.
     */
    public int value()
    {
        return number;
    }

    public char lower()
    {
        return noShiftChar;
    }

    public char upper()
    {
        return shiftChar;
    }

    public boolean printable()
    {
        return noShiftChar != '\u0000' && shiftChar != '\u0000';
    }
}
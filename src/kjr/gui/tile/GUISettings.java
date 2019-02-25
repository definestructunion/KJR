package kjr.gui.tile;

import kjr.util.DeepCopy;
import kjr.gfx.Colour;
import kjr.gui.ColourTheme;

public class GUISettings implements DeepCopy<GUISettings>
{
    public static char defaultColumnGlyph;
    public static char defaultRowGlyph;
    public static char defaultLeftTitleGlyph;
    public static char defaultRightTitleGlyph;

    public static char defaultGlyphSize;

    public static char defaultTopRightGlyph;
    public static char defaultTopLeftGlyph;
    public static char defaultBottomRightGlyph;
    public static char defaultBottomLeftGlyph;
    
    public static ColourTheme defaultColourTheme;

    static
    {
        defaultColumnGlyph      = 186;
        defaultRowGlyph         = 205;
        defaultLeftTitleGlyph   = 185;
        defaultRightTitleGlyph  = 204;

        defaultGlyphSize        = 16;

        defaultTopRightGlyph    = 187;
        defaultTopLeftGlyph     = 201;
        defaultBottomRightGlyph = 188;
        defaultBottomLeftGlyph  = 200;
        defaultColourTheme      = new ColourTheme(new Colour(1, 1, 1, 1), new Colour(0, 0, 0, 1));
    }

    public char columnGlyph,
                rowGlyph,
                leftTitleGlyph,
                rightTitleGlyph,
                topRightGlyph,
                topLeftGlyph,
                bottomRightGlyph,
                bottomLeftGlyph;

    public int glyphSize;

    public ColourTheme colourTheme;

    public GUISettings()
    {
        setToDefaults();
    }

    public void setToDefaults()
    {
        columnGlyph      = defaultColumnGlyph;
        rowGlyph         = defaultRowGlyph;
        leftTitleGlyph   = defaultLeftTitleGlyph;
        rightTitleGlyph  = defaultRightTitleGlyph;
        glyphSize        = defaultGlyphSize;

        topRightGlyph    = defaultTopRightGlyph;
        topLeftGlyph     = defaultTopLeftGlyph;
        bottomRightGlyph = defaultBottomRightGlyph;
        bottomLeftGlyph  = defaultBottomLeftGlyph;

        colourTheme      = defaultColourTheme;
    }

    @Override
    public GUISettings copy()
    {
        GUISettings obj      = new GUISettings();

        obj.columnGlyph      = columnGlyph;
        obj.rowGlyph         = rowGlyph;
        obj.leftTitleGlyph   = leftTitleGlyph;
        obj.rightTitleGlyph  = rightTitleGlyph;
        obj.glyphSize        = glyphSize;
        obj.colourTheme      = colourTheme.copy();
        obj.topLeftGlyph     = topLeftGlyph;
        obj.topRightGlyph    = topRightGlyph;
        obj.bottomLeftGlyph  = bottomLeftGlyph;
        obj.bottomRightGlyph = bottomRightGlyph;

        return obj;
    }
}
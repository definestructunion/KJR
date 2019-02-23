package kjr.gui.tile;

import kjr.util.DeepCopy;
import kjr.gfx.Colour;
import kjr.gui.ColourTheme;

public class GUISettings implements DeepCopy<GUISettings>
{
    public static char defaultColumnGlyph      = 186;
    public static char defaultRowGlyph         = 205;
    public static char defaultLeftTitleGlyph   = 185;
    public static char defaultRightTitleGlyph  = 204;

    public static char defaultGlyphSize        = 16;

    public static char defaultTopRightGlyph    = 187;
    public static char defaultTopLeftGlyph     = 201;
    public static char defaultBottomRightGlyph = 188;
    public static char defaultBottomLeftGlyph  = 200;
    
    public static ColourTheme defaultColourTheme = new ColourTheme(new Colour(1, 1, 1, 1), new Colour(0, 0, 0, 1));

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
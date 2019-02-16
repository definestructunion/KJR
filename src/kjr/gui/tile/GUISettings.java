package kjr.gui.tile;

import kjr.util.DeepCopy;
import kjr.gfx.Colour;

public class GUISettings implements DeepCopy<GUISettings>
{
    public static int defaultColumnGlyph = 100;
    public static int defaultRowGlyph = 100;
    public static int defaultLeftTitleGlyph = 100;
    public static int defaultRightTitleGlyph = 100;
    public static float defaultBorderScale = 1.0f;
    
    public static Colour defaultInnerColour = new Colour(0, 0, 0, 1);
    public static Colour defaultBorderColour = new Colour(1, 1, 1, 1);

    public int columnGlyph,
               rowGlyph,
               leftTitleGlyph,
               rightTitleGlyph;
               
    public float borderScale;

    public Colour borderColour;
    public Colour innerColour;

    public GUISettings()
    {
        setToDefaults();
    }

    public void setToDefaults()
    {
        columnGlyph     = defaultColumnGlyph;
        rowGlyph        = defaultRowGlyph;
        leftTitleGlyph  = defaultLeftTitleGlyph;
        rightTitleGlyph = defaultRightTitleGlyph;
        borderScale     = defaultBorderScale;
        borderColour    = defaultBorderColour;
        innerColour     = defaultInnerColour;
    }

    @Override
    public GUISettings copy()
    {
        GUISettings obj     = new GUISettings();

        obj.columnGlyph     = columnGlyph;
        obj.rowGlyph        = rowGlyph;
        obj.leftTitleGlyph  = leftTitleGlyph;
        obj.rightTitleGlyph = rightTitleGlyph;
        obj.borderScale     = borderScale;
        obj.borderColour    = borderColour;
        obj.innerColour     = innerColour;

        return obj;
    }
}
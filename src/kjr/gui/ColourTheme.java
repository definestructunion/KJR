package kjr.gui;

import kjr.gfx.Colour;
import kjr.util.DeepCopy;

public class ColourTheme implements DeepCopy<ColourTheme>
{
    // TODO: add default colour themes

    private Colour border = null;
    private Colour inner = null;

    public ColourTheme(Colour border, Colour inner)
    {
        this.border = border.copy();
        this.inner = inner.copy();
    }

    public Colour getBorder() { return border; }
    public void setBorder(Colour colour) { border = colour; }

    public Colour getInner() { return inner; }
    public void setInner(Colour colour) { inner = colour; }

    @Override
    public ColourTheme copy()
    {
        return new ColourTheme(border, inner);
    }
}
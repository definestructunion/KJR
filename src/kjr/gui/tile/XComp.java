package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.SpriteBatch;
import kjr.gui.Align;
import kjr.gui.Func;

public abstract class XComp
{
    protected Box box;
    protected Align align;

    protected Func highlight;
    protected Func activate;
    protected Func keyPress;
    protected Func buttonPress;

    public Box getBox() { return box; }
    public void setBox(Box value) { box = value; }

    public Align getAlign() { return align; }
    public void setAlign(Align value) { align = value; }

    public void setHighlight(Func value) { highlight = value; }
    public void setActivate(Func value) { activate = value; }
    public void setKeyPress(Func value) { keyPress = value; }
    public void setButtonPress(Func value) { buttonPress = value; }

    abstract public void draw(XConsole console, SpriteBatch renderer);
    abstract public void update();
}
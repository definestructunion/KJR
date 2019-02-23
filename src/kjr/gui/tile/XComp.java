package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.Renderer;
import kjr.gfx.SpriteBatch;
import kjr.gui.Align;
import kjr.gui.Func;

public abstract class XComp
{
    protected Box box;
    protected Align align;

    protected Func keyPress;
    protected Func buttonPress;
    protected Func update;

    protected boolean activated = false;

    public Box getBox() { return box; }
    public void setBox(Box value) { box = value; }

    public Align getAlign() { return align; }
    public void setAlign(Align value) { align = value; }

    public void setKeyPress(Func value) { keyPress = value; }
    public void setButtonPress(Func value) { buttonPress = value; }
    public void setUpdate(Func value) { update = value; }

    abstract public void draw(XConsole console, SpriteBatch renderer);
    abstract public void update();

    protected XComp(Box box, Align align)
    {
        this.box = box;
        this.align = align;

        keyPress = () -> { };
        buttonPress = () -> { };
        update = () -> { };
    }
}
package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.Renderer;
import kjr.gui.Align;
import kjr.gui.Func;

public abstract class XComp
{
    protected Box box;
    protected Box alignedBox;
    protected Align align = Align.None;
    protected XConsole console;

    protected Func keyPress;
    protected Func buttonPress;
    protected Func update;

    protected boolean activated = false;

    public Box getBox() { return box; }
    public void setBox(Box value) { box = value; alignBox(); }

    public Align getAlign() { return align; }
    public void setAlign(Align value) { align = value; alignBox(); }

    public void setKeyPress(Func value) { keyPress = value; }
    public void setButtonPress(Func value) { buttonPress = value; }
    public void setUpdate(Func value) { update = value; }

    abstract public void draw(Renderer renderer);
    abstract public void update();

    protected XComp(Box box, XConsole console)
    {
        this.console = console;
        this.box = box;
        this.alignedBox = this.box.copy();
        alignBox();
        keyPress = () -> { };
        buttonPress = () -> { };
        update = () -> { };
    }

    protected XComp(Box box, XConsole console, Align align)
    {
        this.console = console;
        this.box = box;
        this.alignedBox = this.box.copy();
        alignBox();
        this.align = align;
        keyPress = () -> { };
        buttonPress = () -> { };
        update = () -> { };
    }

    protected void alignBox()
    {
        Box consoleBox = console.getBox();
        alignedBox.x = consoleBox.x + box.x;
        alignedBox.y = consoleBox.y + box.y;

        align.toPosition(consoleBox, alignedBox);

        if(align != Align.None)
        {
            alignedBox.x += box.x;
            alignedBox.y += box.y;
        }

        alignedBox.width = box.width;
        alignedBox.height = box.height;
    }
}
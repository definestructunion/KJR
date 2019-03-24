package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.Colour;
import kjr.gfx.Renderer;

public class Label extends XComp
{
    private String text;
    private boolean wrapping = false;

    public Label(String text, int x, int y, XConsole console)
    {
        super(new Box(x, y, (console.getBox().width - 1), 1), console);
        this.text = text;
    }

    @Override
    public void update()
    {

    }

    @Override
    public void draw(Renderer renderer)
    {
        alignBox();

        Colour border = console.getColourTheme().getBorder();
        Colour inner = console.getColourTheme().getInner();

        int xPos = 0;
        int yPos = 0;

        for(int i = 0; i < text.length(); ++i)
        {
            if(wrapping && xPos >= (console.getBox().width - 1) - box.x)
            {
                xPos = 0;
                ++yPos;
            }

            renderer.draw(inner, alignedBox.x + xPos, alignedBox.y + yPos, 0.0f);
            renderer.draw(text.charAt(i), border, alignedBox.x + xPos, alignedBox.y + yPos, 0.0f);
            ++xPos;
        }

        box.width = (console.getBox().width - 1) - box.x;
        box.height = yPos + 1;
    }

    public String getText() { box.width = text.length(); return text; }
    public void setText(String value) { text = value; }
    public void setWrapping(boolean value) { wrapping = value; }
    public boolean getWrapping() { return wrapping; }
}

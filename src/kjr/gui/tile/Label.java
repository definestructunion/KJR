package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.Colour;
import kjr.gfx.Renderer;

public class Label extends XComp
{
    private String text;

    public Label(String text, int x, int y, XConsole console)
    {
        super(new Box(x, y, text.length(), 1), console);
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

        for(int i = 0; i < text.length(); ++i)
        {
            renderer.draw(inner, alignedBox.x + i, alignedBox.y, 0.0f);
            renderer.draw(text.charAt(i), border, alignedBox.x + i, alignedBox.y, 0.0f);
        }
    }

    public String getText() { box.width = text.length(); return text; }
    public Label setText(String value) { text = value; return this; }
}

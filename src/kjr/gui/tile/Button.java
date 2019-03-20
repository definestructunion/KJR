package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.Colour;
import kjr.gfx.Renderer;
import kjr.input.Buttons;
import kjr.input.Input;

public class Button extends XComp
{
    protected String message = "";

    protected boolean highlighting = false;

    public String getMessage() { return message; }
    public void setMessage(String value) { message = value; }

    public Button(String message, XConsole console)
    {
        super(new Box(), console);
        this.message = message;
    }

    public Button(String message, int xOffset, int yOffset, XConsole console)
    {
        super(new Box(xOffset, yOffset, message.length(), 1), console);
        this.message = message;
    }

    @Override
    public void draw(Renderer renderer)
    {
        box.height = 1;
        box.width = message.length();
        alignBox();

        Colour inner = (highlighting) ? console.getColourTheme().getBorder() : console.getColourTheme().getInner();
        Colour border = (highlighting) ? console.getColourTheme().getInner() : console.getColourTheme().getBorder();

        for(int x = 0; x < box.width; ++x)
        {
            renderer.draw(inner, alignedBox.x + x, alignedBox.y, 0.0f);

            if(x < message.length())
                renderer.draw(message.charAt(x), border, alignedBox.x + x, alignedBox.y, 0.0f);
        }
    }

    @Override
    public void update()
    {
        if(activated)
            update.call();

        if(alignedBox.asRect(console.getGlyphSize()).contains(Input.getMousePosition()))
        {
            highlighting = true;
            if(Input.buttonPressed(Buttons.Left))
                activated = true;
        }
        else
            highlighting = false;
    }
}
package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.Colour;
import kjr.gfx.SpriteBatch;
import kjr.gui.Align;
import kjr.input.Buttons;
import kjr.input.Input;

public class Button extends XComp
{
    protected String message = "";

    protected Box actualBox = new Box();

    protected boolean highlighting = false;

    public String getMessage() { return message; }
    public void setMessage(String value) { message = value; }

    public Button(String message)
    {
        super(new Box(), Align.TopLeft);
        this.message = message;
    }

    @Override
    public void update()
    {
        if(activated)
            update.call();

        if(box.asRect(16).contains(Input.getMousePosition()))
        {
            highlighting = true;
            if(Input.buttonPressed(Buttons.Left))
                activated = true;
        }
        else
            highlighting = false;
    }

    @Override
    public void draw(XConsole console, SpriteBatch renderer)
    {
        box.width = message.length();
        
        box = align.toPosition(console.getBox(), box);

        for(int x = 0; x < box.width; ++x)
        {
            Colour inner = (highlighting) ? console.getColourTheme().getBorder() : console.getColourTheme().getInner();
            Colour border = (highlighting) ? console.getColourTheme().getInner() : console.getColourTheme().getBorder();

            renderer.draw(inner, box.x + x, box.y, 1.0f);

            if(x < message.length())
                renderer.draw(message.charAt(x), border, box.x + x, box.y, 1.1f);
        }
    }
}
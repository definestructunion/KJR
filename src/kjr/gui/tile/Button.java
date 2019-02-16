package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.SpriteBatch;
import kjr.gui.Align;

public class Button extends XComp
{
    protected String message = "";

    public String getMessage() { return message; }
    public void setMessage(String value) { message = value; }

    public Button(String message)
    {
        this.message = message;
    }

    @Override
    public void update()
    {
        
    }

    @Override
    public void draw(XConsole console, SpriteBatch renderer)
    {
        if(message.length() > box.width)
            box.width = message.length();
        
        box = align.toPosition(console.getBox(), box);

        for(int x = 0; x < box.width; ++x)
        {
            renderer.draw(console.getColourTheme().getInner(), box.x + x, box.y, 0.9f);
            
            if(x < message.length())
                renderer.drawGlyph(message.charAt(x), console.getColourTheme().getBorder(), 2.0f, box.x + x, box.y, 1.0f);
        }
    }
}
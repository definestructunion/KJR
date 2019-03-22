package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.Renderer;
import kjr.input.Input;
import kjr.input.Keys;

public class Textbox extends XComp
{
    private StringBuilder text = new StringBuilder();

    public Textbox(Box box, XConsole console)
    {
        super(box, console);
    }

    @Override
    public void draw(Renderer renderer)
    {
        alignBox();

        if(widthZero())
            return;

        int xPos = 0;
        int yPos = 0;

        for(int i = 0; i < text.length(); ++i)
        {
            if(text.charAt(i) == '\n')
            {
                ++yPos;
                xPos = 0;
                continue;
            }

            if(xPos != 0 && xPos % alignedBox.width == 0)
            {
                ++yPos;
                xPos = 0;
            }

            if(yPos >= alignedBox.height)
                return;

            renderer.draw(text.charAt(i), console.getColourTheme().getBorder(), alignedBox.x + xPos, alignedBox.y + yPos, 0.0f);
            ++xPos;
        }
    }

    @Override
    public void update()
    {
        alignBox();

        boolean shift = Input.keyDown(Keys.LeftShift) || Input.keyDown(Keys.RightShift);

        for(int i = 1; i < Keys.values().length; ++i)
        {
            Keys key = Keys.values()[i];
            if (key.printable() && Input.keyPressed(key))
                text.append((shift) ? key.upper() : key.lower());
        }

        if(Input.keyPressed(Keys.Backspace) && text.length() > 0)
            text.delete(text.length() - 1, text.length());
    }
    
    public String getText() { return text.toString(); }
    public void setText(String value) { text.setLength(0); text.append(value); }

    private boolean widthZero() { return alignedBox.width == 0; }
}

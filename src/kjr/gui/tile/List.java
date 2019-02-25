package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.Colour;
import kjr.gfx.SpriteBatch;
import kjr.gui.Align;

import java.util.ArrayList;

public class List extends XComp
{
    private ArrayList<ListItem> items = new ArrayList<>();

    public List(Box box, XConsole console)
    {
        super(box, console);
    }

    public List(Box box, XConsole console, Align align)
    {
        super(box, console, align);
    }

    public void add(ListItem... items)
    {
        for(ListItem item : items)
            this.items.add(item);
    }

    @Override
    public void draw(SpriteBatch renderer)
    {
        int line = 0;
        for(ListItem item : items)
        {
            boolean itemHasTexture = item.texture != null;

            if(itemHasTexture)
                renderer.draw(item.texture, Colour.white, alignedBox.x, alignedBox.y + line, 0.0f);

            for(int i = 0; i < item.text.length(); ++i)
            {
                int posX = alignedBox.x + i + ((itemHasTexture) ? 1 : 0);
                renderer.draw(item.text.charAt(i), Colour.white, posX, alignedBox.y + line, 0.0f);
            }

            ++line;
        }
    }

    @Override
    public void update()
    {
        alignBox();
    }
}
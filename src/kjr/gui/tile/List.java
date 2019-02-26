package kjr.gui.tile;

import javafx.scene.control.Label;
import kjr.gfx.Box;
import kjr.gfx.Colour;
import kjr.gfx.SpriteBatch;
import kjr.gui.Align;
import kjr.input.Buttons;
import kjr.input.Input;

import java.util.ArrayList;
import java.util.Arrays;

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

    public void add(ListItem... items) { this.items.addAll(Arrays.asList(items)); }

    public void remove(ListItem item) { this.items.remove(item); }

    public boolean hasRef(ListItem item) { return this.items.contains(item); }

    public ArrayList<ListItem> getItems() { return items; }

    @Override
    public void draw(SpriteBatch renderer)
    {
        alignBox();
        int line = 0;

        int posX = 0;
        int posY = 0;

        for(int index = 0; index < items.size(); ++index)
        {
            ListItem item = items.get(index);
            item.wrapToBox(alignedBox, index);
            boolean itemHasTexture = item.texture != null;

            if(itemHasTexture)
                renderer.draw(item.texture, Colour.white, alignedBox.x, alignedBox.y + line, 0.0f);

            for(int i = 0; i < item.text.length(); ++i)
            {
                posX = alignedBox.x + ((itemHasTexture) ? 1 : 0);
                posX += i % alignedBox.width;

                if(i % alignedBox.width == 0 && i != 0)
                    ++line;

                renderer.draw(item.text.charAt(i), console.getColourTheme().getBorder(), posX, alignedBox.y + line, 0.0f);
                //renderer.draw(item.text.charAt(i), console.getColourTheme().getBorder(), alignedBox.x, alignedBox.y, 0.0f);

            }

            ++line;
        }
    }

    @Override
    public void update()
    {
        for(int i = 0; i < items.size(); ++i)
        {
            int previousYOffset = (i > 0) ? items.get(i - 1).box.height - 1 : 0;
            ListItem item = items.get(i);
            item.wrapToBox(alignedBox, i + previousYOffset);

            //System.out.println(item.box.toString());

            // if box contains the mouse position and clicked it
            if(item.box.asRect(console.getGlyphSize()).contains(Input.getMousePosition()) && Input.buttonPressed(Buttons.Left))
                item.onActivate.call();
        }
    }
}
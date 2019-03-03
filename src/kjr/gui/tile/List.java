package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.Colour;
import kjr.gfx.SpriteBatch;
import kjr.input.Buttons;
import kjr.input.Input;

import java.util.ArrayList;
import java.util.Arrays;

public class List extends XComp
{
    private ArrayList<ListItem> items = new ArrayList<>();
    private int listOffset = 0;

    private ListItem lastNode = null;

    public List(Box box, XConsole console)
    {
        super(box, console);
    }

    @Override
    public void draw(SpriteBatch renderer)
    {
        alignBox();

        int line = 0;

        for(int index = 0; index < items.size() && index + listOffset < items.size(); ++index)
        {
            ListItem item = items.get(index + listOffset);

            boolean highlighting = item.getBox().asRect(console.getGlyphSize()).contains(Input.getMousePosition());
            Colour inner = (highlighting) ? console.getColourTheme().getBorder() : console.getColourTheme().getInner();
            Colour border = (highlighting) ? console.getColourTheme().getInner() : console.getColourTheme().getBorder();

            boolean itemHasTexture = item.getTexture() != null;

            if(itemHasTexture)
                renderer.draw(item.getTexture(), Colour.white, alignedBox.x, alignedBox.y + line, 0.0f);

            for(int i = 0; i < item.getText().length(); ++i)
            {
                int posX = alignedBox.x + ((itemHasTexture) ? 1 : 0);
                posX += i % (alignedBox.width - 1);

                if(i % (alignedBox.width - 1) == 0 && i != 0)
                {
                    ++line;
                    if(line >= alignedBox.height)
                    {
                        lastNode = item;
                        return;
                    }
                }

                renderer.draw(inner, posX, alignedBox.y + line, 0.0f);
                renderer.draw(item.getText().charAt(i), border, posX, alignedBox.y + line, 0.0f);
            }


            ++line;
            if(line >= alignedBox.height)
            {
                lastNode = item;
                return;
            }

            lastNode = item;
        }
    }

    @Override
    public void update()
    {
        alignBox();

        int yOffset = 0;
        for(int i = listOffset; i < items.size(); ++i)
        {
            ListItem item = items.get(i);
            item.wrapToBox(alignedBox, yOffset);
            yOffset += item.measureY(alignedBox);

            if(Input.buttonPressed(Buttons.Left) && item.getBox().asRect(console.getGlyphSize()).contains(Input.getMousePosition()))
                item.getActivate().call();

            if(yOffset >= alignedBox.height)
                break;
        }

        if(alignedBox.asRect(console.getGlyphSize()).contains(Input.getMousePosition()))
        {
            if(Input.getScrollY() < -0.1)
            {
                if(items.size() > 0 && lastNode != items.get(items.size() - 1))
                    ++listOffset;
            }
            else if(Input.getScrollY() > 0.1)
            {
                if(listOffset != 0)
                    --listOffset;
            }
        }
    }

    public void add(ListItem... items) { this.items.addAll(Arrays.asList(items)); }

    public void remove(ListItem item) { this.items.remove(item); }

    public boolean hasRef(ListItem item) { return this.items.contains(item); }

    public ArrayList<ListItem> getItems() { return items; }
}
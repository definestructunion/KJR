package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.Colour;
import kjr.gfx.Renderer;
import kjr.input.Buttons;
import kjr.input.Input;
import kjr.math.Vec2;

import java.util.ArrayList;
import java.util.Arrays;

public class List extends XComp
{
    private ArrayList<ListItem> items = new ArrayList<>();
    private int listOffset = 0;
    private Colour itemColour = new Colour(1, 1, 1, 1);

    public List(Box box, XConsole console)
    {
        super(box, console);
    }

    @Override
    public void draw(Renderer renderer)
    {
        alignBox();

        for(int index = 0; index < items.size(); ++index)
        {
            if(index + listOffset >= items.size())
                return;

            ListItem item = items.get(index + listOffset);

            boolean highlighting = getContains(item, Input.getMousePosition());
            Colour inner = (highlighting) ? console.getColourTheme().getBorder() : console.getColourTheme().getInner();
            Colour border = (highlighting) ? console.getColourTheme().getInner() : console.getColourTheme().getBorder();

            boolean itemHasTexture = item.getTexture() != null;

            if(itemHasTexture)
                renderer.draw(item.getTexture(), itemColour, alignedBox.x, alignedBox.y + index, 0.0f);

            for(int i = 0; i < item.getText().length(); ++i)
            {
                int posX = alignedBox.x + ((itemHasTexture) ? 1 : 0) + i;

                if(posX >= alignedBox.x + alignedBox.width)
                    break;

                renderer.draw(inner, posX, alignedBox.y + index, 0.0f);
                renderer.draw(item.getText().charAt(i), border, posX, alignedBox.y + index, 0.0f);
            }

            if(index >= alignedBox.height - 1)
                return;
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
            item.getBox().set(alignedBox.x, alignedBox.y + yOffset, alignedBox.width, 1);

            if(Input.buttonPressed(Buttons.Left) && getContains(item, Input.getMousePosition()))
                item.getActivate().call();

            ++yOffset;
        }

        if(alignedBox.asRect(console.getGlyphSize()).contains(Input.getMousePosition()))
        {
            if(Input.getScrollY() < -0.1)
            {
                if(items.size() > 0 && listOffset < items.size() - alignedBox.height)
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

    private boolean getContains(ListItem item, Vec2 position)
    {
        return item.getBox().asRect(console.getGlyphSize()).contains(position);
    }
}
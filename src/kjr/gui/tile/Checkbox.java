package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.Colour;
import kjr.gfx.Renderer;
import kjr.input.Input;
import kjr.math.Vec2;

import java.util.ArrayList;
import java.util.Arrays;

public class Checkbox extends XComp
{
    private ArrayList<ListItem> items = new ArrayList<>();
    private int listOffset = 0;

    private String checkedBox = "[X]";
    private String uncheckedBox = "[ ]";

    public Checkbox(Box box, XConsole console)
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
            String itemText = item.getText();

            boolean highlighting = getContains(item, Input.getMousePosition());
            Colour inner = (highlighting) ? console.getColourTheme().getBorder() : console.getColourTheme().getInner();
            Colour border = (highlighting) ? console.getColourTheme().getInner() : console.getColourTheme().getBorder();

            boolean itemSelected = item.getSelected();
            String selectBox = (itemSelected) ? checkedBox : uncheckedBox;

            for(int i = 0; i < selectBox.length(); ++i)
            {
                int posX = alignedBox.x + i;
                int posY = alignedBox.y + index;

                if(posX >= alignedBox.x + alignedBox.width)
                    break;

                renderer.draw(inner, posX, posY, 0.0f);
                renderer.draw(selectBox.charAt(i), border, posX, posY, 0.0f);
            }

            for(int i = 0; i < itemText.length(); ++i)
            {
                int posX = alignedBox.x + i + selectBox.length();
                int posY = alignedBox.y + index;

                if(posX >= alignedBox.x + alignedBox.width)
                    break;

                renderer.draw(inner, posX, posY, 0.0f);
                renderer.draw(itemText.charAt(i), border, posX, posY, 0.0f);
            }

            if(index >= alignedBox.height - 1)
                return;
        }
    }

    @Override
    public void update()
    {
        alignBox();

    }

    public void add(ListItem... items) { this.items.addAll(Arrays.asList(items)); }

    public void remove(ListItem item) { this.items.remove(item); }

    public boolean hasRef(ListItem item) { return this.items.contains(item); }

    public ArrayList<ListItem> getItems() { return items; }

    private boolean getContains(ListItem item, Vec2 position)
    {
        return item.getBox().asRect(console.getGlyphSize()).contains(position);
    }

    public String getCheckedBox() { return checkedBox; }
    public Checkbox setCheckedBox(String value) { checkedBox = value; return this; }

    public String getUncheckedBox() { return uncheckedBox; }
    public Checkbox setUncheckedBox(String value) { uncheckedBox = value; return this; }
}

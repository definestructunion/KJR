package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.Colour;
import kjr.gfx.Renderer;
import kjr.input.Buttons;
import kjr.input.Input;
import java.util.ArrayList;
import java.util.Arrays;

public class Checkbox extends XComp
{
    private ArrayList<ListItem> items = new ArrayList<>();

    private ArrayList<ListItem> selectedItems = new ArrayList<>();
    private int selectCount = 1;

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

            boolean highlighting = mouseOn(item);
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

        int yOffset = 0;
        for(int i = listOffset; i < items.size(); ++i)
        {
            ListItem item = items.get(i);
            item.getBox().set(alignedBox.x, alignedBox.y + yOffset, alignedBox.width, 1);

            if(mouseOnClicked(item))
            {
                boolean selecting = !item.getSelected();

                if(!selecting)
                {
                    item.setSelected(false);
                    selectedItems.remove(item);
                }

                else
                {
                    if(selectedItems.size() >= selectCount)
                    {
                        if (selectedItems.size() > 0)
                        {
                            selectedItems.get(0).setSelected(false);
                            selectedItems.remove(0);
                        }
                    }

                    item.setSelected(true);
                    selectedItems.add(item);
                }
            }

            ++yOffset;
        }

        if(mouseInAlignedBox())
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
    public void add(ArrayList<ListItem> items) { this.items.addAll(items); }

    public void remove(ListItem... items) { this.items.removeAll(Arrays.asList(items)); this.selectedItems.removeAll(Arrays.asList(items)); }
    public void remove(ArrayList<ListItem> items) { this.items.removeAll(items); this.selectedItems.removeAll(items); }
    public void remove(int... indices)
    {
        for(int i = 0; i <  indices.length; ++i)
        {
            ListItem item = items.get(indices[i]);
            remove(item);
        }
    }

    public boolean hasRef(ListItem item) { return this.items.contains(item); }

    public ArrayList<ListItem> getItems() { return items; }

    public ArrayList<ListItem> getSelectedItems() { return new ArrayList<>(selectedItems); }

    private boolean mouseOn(ListItem item)
    {
        return item.getBox().asRect(console.getGlyphSize()).contains(Input.getMousePosition());
    }

    private boolean mouseOnClicked(ListItem item)
    {
        return Input.buttonPressed(Buttons.Left) && mouseOn(item);
    }

    public String getCheckedBox() { return checkedBox; }
    public Checkbox setCheckedBox(String value) { checkedBox = value; return this; }

    public String getUncheckedBox() { return uncheckedBox; }
    public Checkbox setUncheckedBox(String value) { uncheckedBox = value; return this; }

    public Checkbox setSelectCount(int count)
    {
        selectCount = count;
        return this;
    }

    public int getSelectedCount()
    {
        return selectedItems.size();
    }

    public void callAllSelected()
    {
        for(ListItem item : selectedItems)
        {
            item.getActivate().call();
        }
    }

    public void deselectAll()
    {
        for(ListItem item : selectedItems)
            item.setSelected(false);
    }

    public void selectAll()
    {
        for(ListItem item : selectedItems)
            item.setSelected(true);
    }

    public ListItem getHovered()
    {
        for(int i = listOffset; i < items.size(); ++i)
        {
            ListItem item = items.get(i);

            if(mouseOn(item))
                return item;

            if(i - listOffset >= alignedBox.height - 1)
                return null;
        }

        return null;
    }
}

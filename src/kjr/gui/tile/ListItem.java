package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.Texture;
import kjr.gui.Func;

public class ListItem
{
    private Texture texture;
    private String text;
    private Func activate;
    private Box box;
    private boolean selected = false;

    public ListItem(Texture texture, String text, Func onActivate)
    {
        this.texture = texture;
        this.text = text;
        this.activate = onActivate;
        this.box = new Box();
    }

    public ListItem(Texture texture, String text)
    {
        this.texture = texture;
        this.text = text;
        this.activate = () -> { };
        this.box = new Box();
    }

    public ListItem(String text)
    {
        this.texture = null;
        this.text = text;
        this.activate = () -> { };
        this.box = new Box();
    }

    public void wrapToBox(Box wrapper, int yOffset)
    {
        boolean hasTexture = texture != null;

        box.x = wrapper.x;
        box.y = wrapper.y + yOffset;
        box.width = wrapper.width;

        if(hasTexture)
            ++box.width;

        box.height = text.length() / wrapper.width;
        if(text.length() % wrapper.width != 0)
            ++box.height;
    }

    public int measureY(Box wrapper)
    {
        int y = text.length() / wrapper.width;
        if(text.length() % wrapper.width != 0)
            ++y;
        return y;
    }

    public Texture getTexture() { return texture; }
    public ListItem setTexture(Texture value) { texture = value; return this; }

    public String getText() { return text; }
    public ListItem setText(String value) { text = value; return this; }

    public Func getActivate() { return activate; }
    public ListItem setActivate(Func value) { activate = value; return this; }

    public Box getBox() { return box; }
    public ListItem setBox(Box value) { box = value; return this; }

    public boolean getSelected() { return selected; }
    public ListItem setSelected(boolean value) { selected = value; return this; }
}

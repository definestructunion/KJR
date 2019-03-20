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
    public void setTexture(Texture value) { texture = value; }

    public String getText() { return text; }
    public void setText(String value) { text = value; }

    public Func getActivate() { return activate; }
    public void setActivate(Func value) { activate = value; }

    public Box getBox() { return box; }
    public void setBox(Box value) { box = value; }

    public boolean getSelected() { return selected; }
    public void setSelected(boolean value) { selected = value; }
}

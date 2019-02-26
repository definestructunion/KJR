package kjr.gui.tile;

import kjr.gfx.Box;
import kjr.gfx.Texture;
import kjr.gui.Func;

public class ListItem
{
    public Texture texture;
    public String text;
    public Func onActivate;
    public Box box;

    public ListItem(Texture texture, String text, Func onActivate)
    {
        this.texture = texture;
        this.text = text;
        this.onActivate = onActivate;
        this.box = new Box();
    }

    public ListItem(Texture texture, String text)
    {
        this.texture = texture;
        this.text = text;
        this.onActivate = () -> { };
        this.box = new Box();
    }

    public void wrapToBox(Box wrapper, int yOffset)
    {
        boolean hasTexture = texture != null;

        box.x = wrapper.x;
        box.y = wrapper.y + yOffset;
        box.width = wrapper.width;
        box.height = text.length() / wrapper.width;
        if(text.length() % wrapper.width != 0)
            ++box.height;
    }
}

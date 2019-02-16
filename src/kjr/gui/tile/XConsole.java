package kjr.gui.tile;

import java.util.ArrayList;

import kjr.gfx.Box;
import kjr.gfx.Colour;
import kjr.gfx.Font;
import kjr.gfx.SpriteBatch;
import kjr.gui.ColourTheme;

public class XConsole
{
    private Font font = null;
    private String title = null;
    private ColourTheme theme = null;
    private ArrayList<XComp> xcomps = new ArrayList<>();
    private Box box = new Box();
    private int tileSize = 16;

    public Font getFont() { return font; }
    public void setFont(Font value) { font = value; }

    public String getTitle() { return title; }
    public void setTitle(String value) { title = value; }

    public ColourTheme getColourTheme() { return theme; }
    public void setColourTheme(ColourTheme value) { theme = value; }

    public Box getBox() { return box; }
    public void setBox(Box value) { box = value; }

    public int getTileSize() { return tileSize; }
    public void setTileSize(int value) { tileSize = value; }

    public XConsole()
    {
        theme = new ColourTheme(Colour.white, Colour.black);
        title = "";
        font = null;
    }

    public void update()
    {
        for(XComp xcomp : xcomps)
            xcomp.update();
    }

    public void draw(SpriteBatch renderer)
    {
        for(int y = 0; y < box.height; ++y)
            for(int x = 0; x < box.width; ++x)
                renderer.draw(theme.getInner(), box.x + x, box.y + y, 0.9f);

        for(XComp xcomp : xcomps)
            xcomp.draw(this, renderer);
    }

    public void add(XComp comp)
    {
        xcomps.add(comp);
    }
}
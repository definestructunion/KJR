package kjr.gui.tile;

import kjr.gfx.Colour;
import kjr.gfx.Renderer;
import kjr.gfx.SpriteBatch;

public class Wrapper extends XComp
{
    protected XComp wrapperComp = null;
    protected String title = "";

    public Wrapper(String title, XComp compToWrap, XConsole console)
    {
        super(compToWrap.alignedBox, console);
        wrapperComp = compToWrap;
        this.title = title;
    }

    @Override
    public void draw(Renderer renderer)
    {
        box = wrapperComp.alignedBox.copy();
        --box.x;
        --box.y;
        ++box.width;
        ++box.height;

        drawCorners(renderer);
        drawColumns(renderer);
        drawRowBottom(renderer);
        drawRowTop(renderer);
    }

    @Override
    public void update()
    {

    }

    private void drawCorners(Renderer renderer)
    {
        int left = box.x;
        int right = box.x + box.width;
        int top = box.y;
        int bottom = box.y + box.height;

        Colour border = console.getColourTheme().getBorder();
        renderer.draw(console.getTopLeftGlyph(), border, left, top, 0.0f);
        renderer.draw(console.getTopRightGlyph(), border, right, top, 0.0f);
        renderer.draw(console.getBottomLeftGlyph(), border, left, bottom, 0.0f);
        renderer.draw(console.getBottomRightGlyph(), border, right, bottom, 0.0f);
    }

    private void drawColumns(Renderer renderer)
    {
        Colour border = console.getColourTheme().getBorder();

        // y = 0 is occupied by corner
        for(int y = 1; y < box.height; ++y)
        {
            // draw left column
            renderer.draw(console.getColumnGlyph(), border, box.x, box.y + y, 0.0f);

            // draw right column
            renderer.draw(console.getColumnGlyph(), border, box.x + box.width, box.y + y, 0.0f);
        }
    }

    private void drawRowBottom(Renderer renderer)
    {
        Colour border = console.getColourTheme().getBorder();

        // x = 0 is occupied by corner
        for(int x = 1; x < box.width; ++x)
            renderer.draw(console.getRowGlyph(), border, box.x + x, box.y + box.height, 0.0f);
    }

    private void drawRowTop(Renderer renderer)
    {
        Colour border = console.getColourTheme().getBorder();

        int titleStart = 3;
        int titleEnd = titleStart + ((title != null) ? title.length()
                                                     : 0);
        boolean drawingTitle = (title != null) &&
                               (title.length() > box.width - titleStart - 1) &&
                               (title.length() == 0);

        for(int x = 1; x < box.width; ++x)
        {
            if(drawingTitle)
            {
                if(x == titleStart - 1)
                    renderer.draw(console.getLeftTitleGlyph(), border, box.x + x, box.y, 0.0f);
                else if(x == titleEnd)
                    renderer.draw(console.getRightTitleGlyph(), border, box.x + x, box.y, 0.0f);
                else if(x < titleStart || x > titleEnd)
                    renderer.draw(console.getRowGlyph(), border, box.x + x, box.y, 0.0f);
            }

            else
                renderer.draw(console.getRowGlyph(), border, box.x + x, box.y, 0.0f);
        }

        if(drawingTitle)
            for(int i = 0; i < title.length(); ++i)
                renderer.draw(title.charAt(i), border, box.x + titleStart + i, box.y, 0.0f);
    }
}

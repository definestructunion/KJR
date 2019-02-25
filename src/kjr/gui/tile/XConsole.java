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
    private ArrayList<XComp> xcomps = new ArrayList<>();
    private Box box = new Box();
    private GUISettings settings;

    public Font        getFont            ()                         { return font;                                 }
    public void        setFont            (Font value)               { font                      = value;           }
    public String      getTitle           ()                         { return title;                                }
    public void        setTitle           (String value)             { title                     = value;           }
    public Box         getBox             ()                         { return box;                                  }
    public void        setBox             (Box value)                { box                       = value;           }
    public GUISettings getSettings        ()                         { return settings;                             }
    public void        setSettings        (GUISettings value)        { settings                  = value;           }
    public char        getColumnGlyph     ()                         { return settings.columnGlyph;                 }
    public void        setColumnGlyph     (char columnGlyph)         { settings.columnGlyph      = columnGlyph;     }
    public char        getRowGlyph        ()                         { return settings.rowGlyph;                    }
    public void        setRowGlyph        (char rowGlyph)            { settings.rowGlyph         = rowGlyph;        }
    public char        getLeftTitleGlyph  ()                         { return settings.leftTitleGlyph;              }
    public void        setLeftTitleGlyph  (char leftTitleGlyph)      { settings.leftTitleGlyph   = leftTitleGlyph;  }
    public char        getRightTitleGlyph ()                         { return settings.rightTitleGlyph;             }
    public void        setRightTitleGlyph (char rightTitleGlyph)     { settings.rightTitleGlyph  = rightTitleGlyph; }
    public int         getGlyphSize       ()                         { return settings.glyphSize;                   }
    public void        setGlyphSize       (int glyphSize)            { settings.glyphSize        = glyphSize;       }
    public ColourTheme getColourTheme     ()                         { return settings.colourTheme;                 }
    public void        setColourTheme     (ColourTheme colourTheme)  { settings.colourTheme      = colourTheme;     }
    public char        getTopRightGlyph   ()                         { return settings.topRightGlyph;               }
    public void        setTopRightGlyph   (char value)               { settings.topRightGlyph    = value;           }
    public char        getTopLeftGlyph    ()                         { return settings.topLeftGlyph;                }
    public void        setTopLeftGlyph    (char value)               { settings.topLeftGlyph     = value;           }
    public char        getBottomRightGlyph()                         { return settings.bottomRightGlyph;            }
    public void        setBottomRightGlyph(char value)               { settings.bottomRightGlyph = value;           }
    public char        getBottomLeftGlyph ()                         { return settings.bottomLeftGlyph;             }
    public void        setBottomLeftGlyph (char value)               { settings.bottomLeftGlyph  = value;           }

    public void add(XComp comp)
    {
        xcomps.add(comp);
    }

    public XConsole()
    {
        settings = new GUISettings();
        title = "";
        if(Font.getSize() > 0)
            font = Font.get(0);
        else
            font = null;
        XGUI.add(this);
    }

    public XConsole(String title)
    {
        settings = new GUISettings();
        this.title = title;
        if(Font.getSize() > 0)
            font = Font.get(0);
        else
            font = null;
        XGUI.add(this);
    }

    public void update()
    {
        for(XComp xcomp : xcomps)
            xcomp.update();
    }

    public void draw(SpriteBatch renderer)
    {
        renderer.pushFont(font);

        int oldTileSize = renderer.tileSize;
        renderer.tileSize = settings.glyphSize;

        for(int y = 0; y < box.height + 1; ++y)
            for(int x = 0; x < box.width + 1; ++x)
                renderer.draw(settings.colourTheme.getInner(), box.x + x, box.y + y, 0.0f);

        drawCorners(renderer);
        drawColumns(renderer);
        drawRowBottom(renderer);
        drawRowTop(renderer);

        for(XComp xcomp : xcomps)
            xcomp.draw(renderer);

        renderer.tileSize = oldTileSize;
        renderer.popFont();
    }

    private void drawCorners(SpriteBatch renderer)
    {
        int left = box.x;
        int right = box.x + box.width;
        int top = box.y;
        int bottom = box.y + box.height;

        Colour border = getColourTheme().getBorder();
        renderer.draw(getTopLeftGlyph(), border, left, top, 0.0f);
        renderer.draw(getTopRightGlyph(), border, right, top, 0.0f);
        renderer.draw(getBottomLeftGlyph(), border, left, bottom, 0.0f);
        renderer.draw(getBottomRightGlyph(), border, right, bottom, 0.0f);
    }

    private void drawColumns(SpriteBatch renderer)
    {
        Colour border = getColourTheme().getBorder();

        // y = 0 is occupied by corner
        for(int y = 1; y < box.height; ++y)
        {
            // draw left column
            renderer.draw(getColumnGlyph(), border, box.x, box.y + y, 0.0f);

            // draw right column
            renderer.draw(getColumnGlyph(), border, box.x + box.width, box.y + y, 0.0f);
        }
    }

    private void drawRowBottom(SpriteBatch renderer)
    {
        Colour border = getColourTheme().getBorder();

        // x = 0 is occupied by corner
        for(int x = 1; x < box.width; ++x)
            renderer.draw(getRowGlyph(), border, box.x + x, box.y + box.height, 0.0f);
    }

    private void drawRowTop(SpriteBatch renderer)
    {
        Colour border = getColourTheme().getBorder();
        int titleStart = 3;
        int titleEnd = titleStart + title.length();
        boolean drawingTitle = title.length() > box.width - titleStart - 1 || title.length() == 0;

        for(int x = 1; x < box.width; ++x)
        {
            if(!drawingTitle)
            {
                if(x == titleStart - 1)
                    renderer.draw(getLeftTitleGlyph(), border, box.x + x, box.y, 0.0f);
                else if(x == titleEnd)
                    renderer.draw(getRightTitleGlyph(), border, box.x + x, box.y, 0.0f);
                else if(x < titleStart || x > titleEnd)
                    renderer.draw(getRowGlyph(), border, box.x + x, box.y, 0.0f);
            }

            else
                renderer.draw(getRowGlyph(), border, box.x + x, box.y, 0.0f);
        }

        if(!drawingTitle)
            for(int i = 0; i < title.length(); ++i)
                renderer.draw(title.charAt(i), border, box.x + titleStart + i, box.y, 0.0f);
    }
}
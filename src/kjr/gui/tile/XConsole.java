package kjr.gui.tile;

import java.util.ArrayList;

import kjr.gfx.*;
import kjr.gui.ColourTheme;

public class XConsole
{
    private Font font = null;
    private String title = null;
    private ArrayList<XComp> xcomps = new ArrayList<>();
    private Box box = new Box();
    private GUISettings settings;

    private int x;
    private int y;
    private int width;
    private int height;

    private void setTrueSize()
    {
        x = box.x;
        y = box.y;
        width = box.width - 1;
        height = box.height - 1;
    }

    public Font        getFont            ()                         { return font;                                      }
    public void        setFont            (Font value)               { font                      = value;                }
    public String      getTitle           ()                         { return title;                                     }
    public void        setTitle           (String value)             { title                     = value;                }
    public Box         getBox             ()                         { return box;                                       }
    public void        setBox             (Box value)                { box                       = value; setTrueSize(); }
    public GUISettings getSettings        ()                         { return settings;                                  }
    public void        setSettings        (GUISettings value)        { settings                  = value;                }
    public char        getColumnGlyph     ()                         { return settings.columnGlyph;                      }
    public void        setColumnGlyph     (char columnGlyph)         { settings.columnGlyph      = columnGlyph;          }
    public char        getRowGlyph        ()                         { return settings.rowGlyph;                         }
    public void        setRowGlyph        (char rowGlyph)            { settings.rowGlyph         = rowGlyph;             }
    public char        getLeftTitleGlyph  ()                         { return settings.leftTitleGlyph;                   }
    public void        setLeftTitleGlyph  (char leftTitleGlyph)      { settings.leftTitleGlyph   = leftTitleGlyph;       }
    public char        getRightTitleGlyph ()                         { return settings.rightTitleGlyph;                  }
    public void        setRightTitleGlyph (char rightTitleGlyph)     { settings.rightTitleGlyph  = rightTitleGlyph;      }
    public int         getGlyphSize       ()                         { return settings.glyphSize;                        }
    public void        setGlyphSize       (int glyphSize)            { settings.glyphSize        = glyphSize;            }
    public ColourTheme getColourTheme     ()                         { return settings.colourTheme;                      }
    public void        setColourTheme     (ColourTheme colourTheme)  { settings.colourTheme      = colourTheme;          }
    public char        getTopRightGlyph   ()                         { return settings.topRightGlyph;                    }
    public void        setTopRightGlyph   (char value)               { settings.topRightGlyph    = value;                }
    public char        getTopLeftGlyph    ()                         { return settings.topLeftGlyph;                     }
    public void        setTopLeftGlyph    (char value)               { settings.topLeftGlyph     = value;                }
    public char        getBottomRightGlyph()                         { return settings.bottomRightGlyph;                 }
    public void        setBottomRightGlyph(char value)               { settings.bottomRightGlyph = value;                }
    public char        getBottomLeftGlyph ()                         { return settings.bottomLeftGlyph;                  }
    public void        setBottomLeftGlyph (char value)               { settings.bottomLeftGlyph  = value;                }

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

    public void draw(Renderer renderer)
    {
        renderer.pushFont(font);

        int oldTileSize = renderer.tileSize;
        renderer.tileSize = settings.glyphSize;

        for(int y = 0; y < height + 1; ++y)
            for(int x = 0; x < width + 1; ++x)
                renderer.draw(settings.colourTheme.getInner(), this.x + x, this.y + y, 0.0f);

        drawCorners(renderer);
        drawColumns(renderer);
        drawRowBottom(renderer);
        drawRowTop(renderer);

        for(XComp xcomp : xcomps)
            xcomp.draw(renderer);

        renderer.tileSize = oldTileSize;
        renderer.popFont();
    }

    private void drawCorners(Renderer renderer)
    {
        int left = x;
        int right = x + width;
        int top = y;
        int bottom = y + height;

        Colour border = getColourTheme().getBorder();
        renderer.draw(getTopLeftGlyph(), border, left, top, 0.0f);
        renderer.draw(getTopRightGlyph(), border, right, top, 0.0f);
        renderer.draw(getBottomLeftGlyph(), border, left, bottom, 0.0f);
        renderer.draw(getBottomRightGlyph(), border, right, bottom, 0.0f);
    }

    private void drawColumns(Renderer renderer)
    {
        Colour border = getColourTheme().getBorder();

        // y = 0 is occupied by corner
        for(int y = 1; y < height; ++y)
        {
            // draw left column
            renderer.draw(getColumnGlyph(), border, this.x, this.y + y, 0.0f);

            // draw right column
            renderer.draw(getColumnGlyph(), border, this.x + width, this.y + y, 0.0f);
        }
    }

    private void drawRowBottom(Renderer renderer)
    {
        Colour border = getColourTheme().getBorder();

        // x = 0 is occupied by corner
        for(int x = 1; x < width; ++x)
            renderer.draw(getRowGlyph(), border, this.x + x, this.y + height, 0.0f);
    }

    private void drawRowTop(Renderer renderer)
    {
        Colour border = getColourTheme().getBorder();
        int titleStart = 3;
        int titleEnd = titleStart + title.length();
        boolean drawingTitle = title.length() > width - titleStart - 1 || title.length() == 0;

        for(int x = 1; x < width; ++x)
        {
            if(!drawingTitle)
            {
                if(x == titleStart - 1)
                    renderer.draw(getLeftTitleGlyph(), border, this.x + x, this.y, 0.0f);
                else if(x == titleEnd)
                    renderer.draw(getRightTitleGlyph(), border, this.x + x, this.y, 0.0f);
                else if(x < titleStart || x > titleEnd)
                    renderer.draw(getRowGlyph(), border, this.x + x, this.y, 0.0f);
            }

            else
                renderer.draw(getRowGlyph(), border, this.x + x, this.y, 0.0f);
        }

        if(!drawingTitle)
            for(int i = 0; i < title.length(); ++i)
                renderer.draw(title.charAt(i), border, this.x + titleStart + i, this.y, 0.0f);
    }
}
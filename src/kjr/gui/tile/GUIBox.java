package kjr.gui.tile;

import kjr.gfx.Colour;
import kjr.gfx.Rect;
import kjr.gfx.SpriteBatch;

public class GUIBox
{
    public static char defaultLeftGlyph         = 186;
    public static char defaultRightGlyph        = 186;
    public static char defaultTopGlyph          = 205;
    public static char defaultBottomGlyph       = 205;

    public static char defaultTopLeftCorner     = 201;
    public static char defaultTopRightCorner    = 187;
    public static char defaultBottomLeftCorner  = 200;
    public static char defaultBottomRightCorner = 188;
    
    public static char defaultLeftTitleGlyph    = 185;
    public static char defaultRightTitleGlyph   = 204;

    public static float defaultOutlineScale     = 1.0f;

    public static Colour defaultOutlineColour   = null;
    public static Colour defaultInnerColour     = null;

    public static float defaultOutlineLayer     = 1.0f;
    public static float defaultInnerLayer       = 0.9f;

    static
    {
        defaultOutlineColour = new Colour(1, 1, 1, 1);
        defaultInnerColour   = new Colour(0, 0, 0, 1);
    }

    public String box_title;
    public char box_left_glyph;
    public char box_right_glyph;
    public char box_top_glyph;
    public char box_bottom_glyph;

    public char box_top_left_corner;
    public char box_top_right_corner;
    public char box_bottom_left_corner;
    public char box_bottom_right_corner;

    public char box_left_title_glyph;
    public char box_right_title_glyph;

    public float box_outline_scale;

    public Colour box_outline_colour;
    public Colour box_inner_colour;

    public float box_outline_layer;
    public float box_inner_layer;

    public int pos_x = 0;
    public int pos_y = 0;
    public int dim_x = 1;
    public int dim_y = 1;

    private Rect internal_rect = new Rect();

    public GUIBox(String box_title, int pos_x, int pos_y, int dim_x, int dim_y)
    {
        setToDefaults();
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.box_title = box_title;
        this.dim_x = dim_x;
        this.dim_y = dim_y;
    }

    public GUIBox(String box_title, int pos_x, int pos_y, int dim_x, int dim_y, float box_outline_scale)
    {
        setToDefaults();
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.box_title = box_title;
        this.dim_x = dim_x;
        this.dim_y = dim_y;
        this.box_outline_scale = box_outline_scale;
    }

    public void setToDefaults()
    {
        box_left_glyph          = defaultLeftGlyph;
        box_right_glyph         = defaultRightGlyph;
        box_top_glyph           = defaultTopGlyph;
        box_bottom_glyph        = defaultBottomGlyph;
        box_top_left_corner     = defaultTopLeftCorner;
        box_top_right_corner    = defaultTopRightCorner;
        box_bottom_left_corner  = defaultBottomLeftCorner;
        box_bottom_right_corner = defaultBottomRightCorner;
        box_left_title_glyph    = defaultLeftTitleGlyph;
        box_right_title_glyph   = defaultRightTitleGlyph;
        box_outline_scale       = defaultOutlineScale;
        box_outline_colour      = defaultOutlineColour;
        box_inner_colour        = defaultInnerColour;
        box_outline_layer       = defaultOutlineLayer;
        box_inner_layer         = defaultInnerLayer;
    }

    public void draw(SpriteBatch renderer)
    {
        setRect(renderer.tile_size);
        renderer.drawFree(box_inner_colour, internal_rect, box_inner_layer);

        drawCorners(renderer);
        drawLeftOutline(renderer);
        drawRightOutline(renderer);
        drawTopOutline(renderer);
        drawBottomOutline(renderer);
    }

    private void drawCorners(SpriteBatch renderer)
    {
        int left = pos_x;
        int right = pos_x + dim_x + 1;
        int up = pos_y;
        int down = pos_y + dim_y + 1;
        renderer.drawGlyph(box_top_left_corner, box_outline_colour, box_outline_scale, left, up, box_outline_layer);
        renderer.drawGlyph(box_top_right_corner, box_outline_colour, box_outline_scale, right, up, box_outline_layer);
        renderer.drawGlyph(box_bottom_left_corner, box_outline_colour, box_outline_scale, left, down, box_outline_layer);
        renderer.drawGlyph(box_bottom_right_corner, box_outline_colour, box_outline_scale, right, down, box_outline_layer);
    }

    private void drawTopOutline(SpriteBatch renderer)
    {
        int title_len = box_title.length();
        // we don't draw the title if the length of the title is 0
        // aka there's no title
        // also, we don't want to draw the title if it's too big
        // we need space for the corner characters and the 2 characters are drawn
        // before and after the title characters
        boolean draw_title = (box_title.length() > 0) && !(title_len > dim_x - 4);

        if(draw_title)
        {
            int start = pos_x + 1;
            int end = pos_x + dim_x + 1;
            int title_start = start + 2;
            int title_end = title_start + box_title.length() - 1;
            int left_title_glyph_pos = title_start - 1;
            int right_title_glyph_pos = title_end + 1;

            for(int i = 0; i < title_len; ++i)
                renderer.drawGlyph(box_title.charAt(i), box_outline_colour, box_outline_scale, title_start + i, pos_y, box_outline_layer);
            
            renderer.drawGlyph(box_left_title_glyph, box_outline_colour, box_outline_scale, left_title_glyph_pos, pos_y, box_outline_layer);
            renderer.drawGlyph(box_right_title_glyph, box_outline_colour, box_outline_scale, right_title_glyph_pos, pos_y, box_outline_layer);

            for(int x = start; x < left_title_glyph_pos; ++x)
                renderer.drawGlyph(box_top_glyph, box_outline_colour, box_outline_scale, x, pos_y, box_outline_layer);

            for(int x = right_title_glyph_pos + 1; x < end; ++x)
                renderer.drawGlyph(box_top_glyph, box_outline_colour, box_outline_scale, x, pos_y, box_outline_layer);
        }

        else
        {
            for(int x = pos_x + 1; x <= pos_x + dim_x; ++x)
                renderer.drawGlyph(box_top_glyph, box_outline_colour, box_outline_scale, x, pos_y, box_outline_layer);
        }
    }

    private void drawBottomOutline(SpriteBatch renderer)
    {
        int y = pos_y + dim_y + 1;
        for(int x = 1; x <= dim_x; ++x)
            renderer.drawGlyph(box_bottom_glyph, box_outline_colour, box_outline_scale, pos_x + x, y, box_outline_layer);
    }

    private void drawRightOutline(SpriteBatch renderer)
    {
        int x = pos_x + dim_x + 1;
        for(int y = 1; y <= dim_y; ++y)
            renderer.drawGlyph(box_right_glyph, box_outline_colour, box_outline_scale, x, pos_y + y, box_outline_scale);
    }

    private void drawLeftOutline(SpriteBatch renderer)
    {
        int x = pos_x;
        for(int y = 1; y <= dim_y; ++y)
            renderer.drawGlyph(box_right_glyph, box_outline_colour, box_outline_scale, x, pos_y + y, box_outline_scale);
    }

    private void setRect(int tile_size)
    {
        internal_rect.x = pos_x * tile_size;
        internal_rect.y = pos_y * tile_size;
        internal_rect.width = (dim_x + 1) * tile_size + tile_size;
        internal_rect.height = (dim_y + 1) * tile_size + tile_size;
    }
}
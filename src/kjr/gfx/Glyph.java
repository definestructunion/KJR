package kjr.gfx;

import kjr.math.Vec2;

public class Glyph
{
    public char id;
    public Vec2[] uv;

    public Glyph(char id, Vec2[] uv)
    {
        this.id = id;
        this.uv = uv;
    }
}
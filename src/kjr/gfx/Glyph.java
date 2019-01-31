package kjr.gfx;

import kjr.math.Vec2;

public class Glyph
{
    public int id;
    public Vec2[] uv;

    public Glyph(int id, Vec2[] uv)
    {
        this.id = id;
        this.uv = uv;
    }
}
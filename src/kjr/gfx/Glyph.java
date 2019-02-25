package kjr.gfx;

import kjr.math.Vec2;

public class Glyph
{
    public final char id;
    public final Vec2[] uv;

    public Glyph(char id, Vec2[] uv)
    {
        if(uv.length != 4)
            throw new IllegalStateException("UV array must be 4 in length.");
        this.id = id;
        this.uv = new Vec2[4];
        System.arraycopy(uv, 0, this.uv, 0, 4);
    }
}
package kjr.math;

import java.io.Serializable;

public class Vec2 implements Serializable
{
    private static final long serialVersionUID = 1L;

    public float x;
    public float y;

    public Vec2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vec2(Vec2 vec)
    {
        this.x = vec.x;
        this.y = vec.y;
    }

    public Vec2 add(Vec2 vec)
    {
        return new Vec2(x + vec.x, y + vec.y);
    }

    public Vec2 sub(Vec2 vec)
    {
        return new Vec2(x - vec.x, y - vec.y);
    }

    public Vec2 mult(Vec2 vec)
    {
        return new Vec2(x * vec.x, y * vec.y);
    }

    public Vec2 div(Vec2 vec)
    {
        return new Vec2(x / vec.x, y / vec.y);
    }

    public Vec2 set(Vec2 vec)
    {
        return new Vec2(vec.x, vec.y);
    }

    @Override public boolean equals(Object obj)
    {
        Vec2 vec = (Vec2)obj;
        return (x == vec.x && y == vec.y);
    }
}
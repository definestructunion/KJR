package kjr.math;

import kjr.util.DeepCopy;

public class Vec2 implements DeepCopy<Vec2>
{
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

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        Vec2 vec = (Vec2)obj;
        return (x == vec.x && y == vec.y);
    }

    @Override
    public int hashCode()
    {
        int hashCode = 0;
        hashCode = (hashCode * 397) ^ (int)x;
        hashCode = (hashCode * 397) ^ (int)y;
        return hashCode;
    }

    @Override
    public Vec2 copy()
    {
        return new Vec2(x, y);
    }
}
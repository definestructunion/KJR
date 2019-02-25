package kjr.math;

import kjr.util.DeepCopy;

public class Vec4 implements DeepCopy<Vec4>
{
    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;
    public float w = 0.0f;

    public Vec4(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4(Vec4 vec)
    {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        this.w = vec.w;
    }

    public Vec4 add(Vec4 vec)
    {
        return new Vec4(x + vec.x, y + vec.y, z + vec.z, w + vec.w);
    }

    public Vec4 sub(Vec4 vec)
    {
        return new Vec4(x - vec.x, y - vec.y, z - vec.z, w - vec.w);
    }

    public Vec4 mult(float value)
    {
        return new Vec4(x * value, y * value, z * value, w * value);
    }

    public Vec4 mult(Vec4 vec)
    {
        return new Vec4(x * vec.x, y * vec.y, z * vec.z, w * vec.w);
    }

    public Vec4 div(Vec4 vec)
    {
        return new Vec4(x / vec.x, y / vec.y, z / vec.z, w / vec.w);
    }

    public Vec4 set(Vec4 vec)
    {
        return new Vec4(vec.x, vec.y, vec.z, vec.w);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        Vec4 vec = (Vec4)obj;
        return (x == vec.x && y == vec.y && z == vec.z && w == vec.w);
    }

    @Override
    public int hashCode()
    {
        int hashCode = 0;
        hashCode = (hashCode * 397) ^ (int)x;
        hashCode = (hashCode * 397) ^ (int)y;
        hashCode = (hashCode * 397) ^ (int)z;
        hashCode = (hashCode * 397) ^ (int)w;
        return hashCode;
    }

    @Override
    public Vec4 copy()
    {
        return new Vec4(x, y, z, w);
    }
}
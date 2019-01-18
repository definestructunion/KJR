package kjr.math;

import java.io.Serializable;

public class Vec3 implements Serializable
{
    private static final long serialVersionUID = 1L;

    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;

    public Vec3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(Vec3 vec)
    {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public Vec3 add(Vec3 vec)
    {
        return new Vec3(x + vec.x, y + vec.y, z + vec.z);
    }

    public Vec3 sub(Vec3 vec)
    {
        return new Vec3(x - vec.x, y - vec.y, z - vec.z);
    }

    public Vec3 mult(float value)
    {
        return new Vec3(x * value, y * value, z * value);
    }

    public Vec3 mult(Vec3 vec)
    {
        return new Vec3(x * vec.x, y * vec.y, z * vec.z);
    }

    public Vec3 div(Vec3 vec)
    {
        return new Vec3(x / vec.x, y / vec.y, z / vec.z);
    }

    public Vec3 set(Vec3 vec)
    {
        return new Vec3(vec.x, vec.y, vec.z);
    }

    public Vec3 dotProduct(Vec3 vec) {
		double k1 = (y * vec.z) - (z * vec.y);
		double k2 = (z * vec.x) - (x * vec.z);
		double k3 = (x * vec.y) - (y * vec.x);
		return new Vec3((float)k1, (float)k2, (float)k3);
	}

    public Vec3 rotate(Vec3 axis, float angle)
    {
        Vec3 v = new Vec3(this);
        return v.sub(axis.mult((axis.mult(v))).mult((float)Math.cos(angle))).add((axis.dotProduct(v).mult((float)Math.sin(angle)).add((axis.mult((axis.mult(v)))))));
    }

    @Override public boolean equals(Object obj)
    {
        Vec3 vec = (Vec3)obj;
        return (x == vec.x && y == vec.y && z == vec.z);
    }
}
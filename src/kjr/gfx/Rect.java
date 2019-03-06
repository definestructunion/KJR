package kjr.gfx;

import kjr.math.Vec2;
import kjr.util.DeepCopy;

public class Rect implements DeepCopy<Rect>
{
    public float x, y, width, height;

    public Rect()
    {

    }

    public Rect(float x, float y, float width, float height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(float x, float y)
    {
        return ((x > this.x && x < this.x + this.width) && (y > this.y && y < this.y + this.height));
    }

    public boolean contains(Vec2 pos)
    {
        return ((pos.x > this.x && pos.x < this.x + this.width) && (pos.y > this.y && pos.y < this.y + this.height));
    }

    @Override
    public Rect copy()
    {
        Rect obj = new Rect(x, y, width, height);
        return obj;
    }
}
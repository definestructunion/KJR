package kjr.gfx;

import kjr.util.DeepCopy;

public class Box implements DeepCopy<Box>
{
    public int x, y, width, height;

    public Box() { }

    public Box(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rect asRect(int tileSize)
    {
        return new Rect(x * tileSize, y * tileSize, width * tileSize, height * tileSize);
    }

    public void set(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public Box copy()
    {
        return new Box(x, y, width, height);
    }

    @Override
    public String toString()
    {
        return "X: " + x + ", Y: " + y + "\nW: " + width + ", H: " + height;
    }
}
package kjr.gfx;

public class Box
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
}
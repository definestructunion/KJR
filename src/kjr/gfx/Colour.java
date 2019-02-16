package kjr.gfx;

import kjr.util.DeepCopy;

public class Colour implements DeepCopy<Colour>
{
    public static final Colour red;
    public static final Colour green;
    public static final Colour yellow;
    public static final Colour blue;
    public static final Colour orange;
    public static final Colour purple;
    public static final Colour cyan;
    public static final Colour magenta;
    public static final Colour lime;
    public static final Colour pink;
    public static final Colour teal;
    public static final Colour lavender;
    public static final Colour brown;
    public static final Colour beige;
    public static final Colour maroon;
    public static final Colour mint;
    public static final Colour olive;
    public static final Colour apricot;
    public static final Colour navy;
    public static final Colour grey;
    public static final Colour white;
    public static final Colour black;
    public static final Colour darkGrey;
    static
    {
        red      = new Colour(0.902f, 0.098f, 0.294f, 1.0f);
        green    = new Colour(0.23529412f, 0.7058824f, 0.29411766f, 1.0f);
        yellow   = new Colour(1.0f, 0.88235295f, 0.09803922f, 1.0f);
        blue     = new Colour(0.0f, 0.50980395f, 0.78431374f, 1.0f);
        orange   = new Colour(0.9607843f, 0.50980395f, 0.1882353f, 1.0f);
        purple   = new Colour(0.5686275f, 0.11764706f, 0.7058824f, 1.0f);
        cyan     = new Colour(0.27450982f, 0.9411765f, 0.9411765f, 1.0f);
        magenta  = new Colour(0.9411765f, 0.19607843f, 0.9019608f, 1.0f);
        lime     = new Colour(0.8235294f, 0.9607843f, 0.23529412f, 1.0f);
        pink     = new Colour(0.98039216f, 0.74509805f, 0.74509805f, 1.0f);
        teal     = new Colour(0.0f, 0.5019608f, 0.5019608f, 1.0f);
        lavender = new Colour(0.9019608f, 0.74509805f, 1.0f, 1.0f);
        brown    = new Colour(0.6666667f, 0.43137255f, 0.15686275f, 1.0f);
        beige    = new Colour(1.0f, 0.98039216f, 0.78431374f, 1.0f);
        maroon   = new Colour(0.5019608f, 0.0f, 0.0f, 1.0f);
        mint     = new Colour(0.6666667f, 1.0f, 0.7647059f, 1.0f);
        olive    = new Colour(0.5019608f, 0.5019608f, 0.0f, 1.0f);
        apricot  = new Colour(1.0f, 0.84313726f, 0.7058824f, 1.0f);
        navy     = new Colour(0.0f, 0.0f, 0.5019608f, 1.0f);
        grey     = new Colour(0.5019608f, 0.5019608f, 0.5019608f, 1.0f);
        white    = new Colour(1.0f, 1.0f, 1.0f, 1.0f);
        black    = new Colour(0.0f, 0.0f, 0.0f, 1.0f);
        darkGrey = new Colour(0.05f, 0.05f, 0.05f, 1.0f);
    }

    private float r, g, b, a;
    private float hex;

    public Colour()
    {
        setHex();
    }

    public Colour(float r, float g, float b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1.0f;
        setHex();
    }

    public Colour(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        setHex();
    }
    
    private void setHex()
    {
        hex = Float.intBitsToFloat(((int)(a * 255) << 0x0018 | (int)(b * 255) << 0x0010 | (int)(g * 255) << 0x0008 | (int)(r * 255)));
    }

    public float r() { return r; }
    public float g() { return g; }
    public float b() { return b; }
    public float a() { return a; }
    public float hex() { return hex; }

    public void sr(float val) { r = val; setHex(); }
    public void sg(float val) { g = val; setHex(); }
    public void sb(float val) { b = val; setHex(); }
    public void sa(float val) { a = val; setHex(); }

    @Override
    public Colour copy()
    {
        Colour obj = new Colour();
        obj.r      = r;
        obj.g      = g;
        obj.b      = b;
        obj.a      = a;
        obj.hex    = hex;
        return obj;
    }
}
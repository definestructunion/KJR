package kjr.gfx;

import kjr.math.Mathf;
import kjr.util.DeepCopy;

/**
 * A class with four values represents red, green, blue, and alpha channels between 0 and 1.
 * Values outside of 0 and 1 will be {@link kjr.math.Mathf#clamp(float, float, float) clamped}.
 * <p>
 * Colour implements {@link kjr.util.DeepCopy DeepCopy}, so colours can be copied via
 * {@link kjr.gfx.Colour#copy() Colour.copy()}.
 * <p>
 * Colour comes with 23 static final colours to use for convenience.
 */
public class Colour implements DeepCopy<Colour>
{
    private static final Colour red;
    public static Colour red() { return red.copy(); }
    private static final Colour green;
    public static Colour green() { return green.copy(); }
    private static final Colour yellow;
    public static Colour yellow() { return yellow.copy(); }
    private static final Colour blue;
    public static Colour blue() { return blue.copy(); }
    private static final Colour orange;
    public static Colour orange() { return orange.copy(); }
    private static final Colour purple;
    public static Colour purple() { return purple.copy(); }
    private static final Colour cyan;
    public static Colour cyan() { return cyan.copy(); }
    private static final Colour magenta;
    public static Colour magenta() { return magenta.copy(); }
    private static final Colour lime;
    public static Colour lime() { return lime.copy(); }
    private static final Colour pink;
    public static Colour pink() { return pink.copy(); }
    private static final Colour teal;
    public static Colour teal() { return teal.copy(); }
    private static final Colour lavender;
    public static Colour lavender() { return lavender.copy(); }
    private static final Colour brown;
    public static Colour brown() { return brown.copy(); }
    private static final Colour beige;
    public static Colour beige() { return beige.copy(); }
    private static final Colour maroon;
    public static Colour maroon() { return maroon.copy(); }
    private static final Colour mint;
    public static Colour mint() { return mint.copy(); }
    private static final Colour olive;
    public static Colour olive() { return olive.copy(); }
    private static final Colour apricot;
    public static Colour apricot() { return apricot.copy(); }
    private static final Colour navy;
    public static Colour navy() { return navy.copy(); }
    private static final Colour grey;
    public static Colour grey() { return grey.copy(); }
    private static final Colour white;
    public static Colour white() { return white.copy(); }
    private static final Colour black;
    public static Colour black() { return black.copy(); }
    private static final Colour darkGrey;
    public static Colour darkGrey() { return darkGrey.copy(); }
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
        darkGrey = new Colour(0.15f, 0.15f, 0.15f, 1.0f);
    }

    private float r, g, b, a;
    private float hex;

    public Colour()
    {
        setHex();
    }

    public Colour(float r, float g, float b)
    {
        r(r); g(g); b(b); a(1.0f);
        setHex();
    }

    public Colour(float r, float g, float b, float a)
    {
        r(r); g(g); b(b); a(a);
        setHex();
    }
    
    private void setHex()
    {
        hex = Float.intBitsToFloat(((int)(a * 255) << 0x0018 |
                                    (int)(b * 255) << 0x0010 |
                                    (int)(g * 255) << 0x0008 |
                                    (int)(r * 255)));
    }

    public float r() { return r; }
    public float g() { return g; }
    public float b() { return b; }
    public float a() { return a; }
    public float hex() { return hex; }

    public void r(float val) { r = channelClamp(val); setHex(); }
    public void g(float val) { g = channelClamp(val); setHex(); }
    public void b(float val) { b = channelClamp(val); setHex(); }
    public void a(float val) { a = channelClamp(val); setHex(); }

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

    private float channelClamp(float value)
    {
        return Mathf.clamp(value, 0.0f, 1.0f);
    }
}
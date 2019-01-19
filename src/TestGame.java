import kjr.base.GameProgram;
import kjr.gfx.BatchRenderer;
import kjr.gfx.Font;
import kjr.gfx.Texture;
import kjr.math.Vec4;
import kjr.sfx.Audio;

public class TestGame extends GameProgram
{
    private final static String title = "KJR";
    private final static int width = 32 * 30;
    private final static int height = 32 * 20;

    Audio sound = null;
    Audio sound2 = null;
    Font font = null;
    Texture texture;
    Texture texture2;
    Vec4 white = new Vec4(1, 1, 1, 1);
    Vec4 blue = new Vec4(0, 0, 1, 1);

    public TestGame()
    {
        super(title, width, height, true);
        renderer = new BatchRenderer();
    }

    @Override public void loadAssets()
    {
        sound = new Audio("song.ogg", 0.25f, true);
        sound2 = new Audio("sound.ogg");
        font = new Font("PIXELADE.TTF", 20);
        texture = new Texture("test.png");
        texture2 = new Texture("smile.png");
    }

    @Override public void initialize()
    {
        window.show();
    }

    @Override public void update()
    {
        window.clear(0, 0, 0, 0);
    }

    @Override public void draw()
    {
        renderer.begin();

        for(int y = 0; y < 15; ++y)
        {
            for(int x = 0; x < 15; ++x)
            {
                Texture tex = ((x + y) % 2 == 0) ? texture : texture2;
                renderer.draw(tex, x * 16, y * 16, white);
            }
        }

        renderer.drawString("Hello there, welcome to KJR.", font, 50, 300, blue);

        renderer.end();
        renderer.flush();
        window.update();
        window.render();
    }

    @Override public void clean()
    {
        super.clean();
        sound.delete();
        sound2.delete();
        font.delete();
        texture.delete();
        texture2.delete();
    }
}
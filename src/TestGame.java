import kjr.base.GameProgram;
import kjr.gfx.BatchRenderer;
import kjr.gfx.Font;
import kjr.gfx.Shader;
import kjr.gfx.Texture;
import kjr.input.Input;
import kjr.input.Keys;
import kjr.math.Vec4;
import kjr.sfx.Audio;
import kjr.gfx.TileData;

public class TestGame extends GameProgram
{
    private final static String title = "KJR";
    private final static int width = 32 * 30;
    private final static int height = 32 * 20;

    Shader shader;
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
        renderer = new BatchRenderer(TileData.createOffset(16, 50, 50));
    }

    @Override public void loadAssets()
    {
        sound = new Audio("song.ogg", 0.25f, true);
        sound2 = new Audio("sound.ogg");
        font = new Font("PIXELADE.TTF", 20);
        texture = new Texture("test.png");
        texture2 = new Texture("smile.png");
        shader = Shader.createDefault();
    }

    @Override public void initialize()
    {
        window.show();
        sound.play();
    }

    int x_offset = 0;
    int y_offset = 0;
    @Override public void update()
    {
        if(Input.keyDown(Keys.A))
        {
            renderer.tiles.offset_x -= 4;
        }

        if(Input.keyDown(Keys.D))
        {
            renderer.tiles.offset_x += 4;
        }

        if(Input.keyDown(Keys.W))
        {
            renderer.tiles.offset_y -= 4;
        }

        if(Input.keyDown(Keys.S))
        {
            renderer.tiles.offset_y += 4;
        }

        window.clear(0, 0, 0, 0);
    }

    @Override public void draw()
    {
        shader.bind();
        renderer.begin();

        for(int y = 0; y < 15; ++y)
        {
            for(int x = 0; x < 15; ++x)
            {
                Texture tex = ((x + y) % 2 == 0) ? texture : texture2;
                renderer.draw(tex, x, y, white);
            }
        }

        renderer.drawString("Hello there, welcome to KJR.", font, 50, 300, blue);

        renderer.end();
        renderer.flush();
        window.update();
        window.render();
        shader.unbind();
    }

    @Override public void windowResize()
    {
        super.windowResize();
        System.out.println("Resized");
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
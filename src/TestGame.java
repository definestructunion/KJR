import kjr.base.GameProgram;
import kjr.gfx.SpriteBatch;
import kjr.gfx.Font;
import kjr.gfx.Shader;
import kjr.gfx.Texture;
import kjr.input.Input;
import kjr.input.Keys;
import kjr.math.Mat4;
import kjr.math.Vec4;
import kjr.sfx.Audio;
import kjr.gfx.TileData;
import kjr.gfx.LayerBatch;

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
    SpriteBatch renderer2 = null;
    LayerBatch renderer = null;


    public TestGame()
    {
        super(title, width, height, true);
        renderer2 = new SpriteBatch(TileData.createOffset(16, 50, 50));
        renderer = new LayerBatch(TileData.createOffset(16, 50, 50));
    }

    @Override public void loadAssets()
    {
        //sound = Audio.add("song.ogg", 0.25f, true);
        //sound2 = Audio.add("sound.ogg");
        font = Font.add("PIXELADE.TTF", 20);
        texture = Texture.add("test.png");
        texture2 = Texture.add("smile.png");
        shader = Shader.createDefault();
    }

    @Override public void initialize()
    {
        window.show();
        //sound.play();
    }

    int x_offset = 0;
    int y_offset = 0;
    @Override public void update()
    {
        if(Input.keyDown(Keys.A))
        {
            renderer2.tiles.offset_x -= 4;
            renderer.tiles.offset_x -= 4;
        }

        if(Input.keyDown(Keys.D))
        {
            renderer2.tiles.offset_x += 4;
            renderer.tiles.offset_x += 4;
        }

        if(Input.keyDown(Keys.W))
        {
            renderer2.tiles.offset_y -= 4;
            renderer.tiles.offset_y -= 4;
        }

        if(Input.keyDown(Keys.S))
        {
            renderer2.tiles.offset_y += 4;
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
                //int layer = ((x + y) % 2 == 0) ? 1 : 0;//(tex == texture) ? 0 : 1;
                int layer = 1;
                tex = texture;
                renderer.drawTile(tex, x, y, 5, white);
            }
        }

        renderer.drawStringFree("KJR", font, 400, 400, 0, blue);

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
        shader.bind();
        shader.setShaderPRMatrix(Mat4.ortho(0, window.getWidth(), 0, window.getHeight(), -10, 10));
        shader.unbind();
    }

    @Override public void clean()
    {
        super.clean();
        renderer.delete();
    }
}
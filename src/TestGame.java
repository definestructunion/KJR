import kjr.base.GameProgram;
import kjr.gfx.SpriteBatch;
import kjr.gfx.Font;
import kjr.gfx.Box;
import kjr.gfx.Colour;
import kjr.gfx.Rect;
import kjr.gfx.Shader;
import kjr.gfx.Texture;
import kjr.gui.Align;
import kjr.gui.tile.Button;
import kjr.gui.tile.XConsole;
import kjr.input.Input;
import kjr.input.Keys;
import kjr.math.Mat4;
import kjr.sfx.Audio;

public class TestGame extends GameProgram
{
    private final static String title = "KJR";
    private final static int width = 32 * 30;
    private final static int height = 32 * 20;

    Shader shader;
    //Audio sound = null;
    //Audio sound2 = null;
    Font font = null;
    Texture texture;
    Texture texture2;
    Font bfont = null;
    Colour white = new Colour(1, 1, 1, 1);
    Colour blue = new Colour(0, 0, 1, 1);
    SpriteBatch renderer = null;

    XConsole gui = new XConsole();

    boolean layered = false;


    public TestGame()
    {
        super(title, width, height, true);
        renderer = new SpriteBatch(16);
        layered = true;
    }

    @Override public void loadAssets()
    {
        //sound = Audio.add("song.ogg", 0.175f, true);
        //sound2 = Audio.add("sound.ogg");
        font = Font.add("res/fonts/bfont8x8trans.png", 8);
        texture = Texture.add("test.png");
        shader = Shader.createDefault(width, height);
        //bfont = new Font("res/fonts/bfont8x8.png", 8);
    }

    @Override public void initialize()
    {
        renderer.setSortModeLayered();
        renderer.pushFont(font);
        window.show();
        //sound.play();
        gui.setBox(new Box(10, 10, 10, 10));
        gui.setFont(font);
        gui.setTileSize(16);
        gui.getColourTheme().setInner(Colour.lavender);
        Button button = new Button("Close");
        button.setBox(new Box(1, 1, 5, 1));
        button.setAlign(Align.BottomRight);
        gui.add(button);
    }

    int x_offset = 0;
    int y_offset = 0;
    @Override public void update()
    {
        if(Input.keyDown(Keys.Escape))
        {
            close();
        }

        if(Input.keyDown(Keys.RightShift))
        {
            renderer.setSortModeDeferred();
            layered = false;
        }

        if(Input.keyDown(Keys.LeftShift))
        {
            renderer.setSortModeLayered();
            layered = true;
        }

        if(Input.keyDown(Keys.Enter))
            draw();
        /*if(Input.keyDown(Keys.RightShift))
        {
            window.clear(0.05f, 0.05f, 0.05f, 1.0f);
            window.render();
        }*/
        window.clear(0.05f, 0.05f, 0.05f, 1.0f);
    }

    @Override public void draw()
    {
        shader.bind();
        renderer.begin();


        /*for(int y = 0; y < 16; ++y)
            for(int x = 0; x < 16; ++x)
                renderer.draw(texture, Colour.magenta, x, y, 1.5f);

        renderer.drawFree(texture, Colour.white, new Rect(100, 100, 50, 50), 0.5f);

        renderer.drawFree(Colour.lavender, new Rect(20, 20, 100, 100), 0.5f);*/


        renderer.drawString("Testing\nTesting", Colour.lime, 2.0f, 1, 1, 5.0f);

        gui.draw(renderer);

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
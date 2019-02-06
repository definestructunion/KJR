import kjr.base.GameProgram;
import kjr.gfx.SpriteBatch;
import kjr.gfx.Font;
import kjr.gfx.Colour;
import kjr.gfx.Rect;
import kjr.gfx.Shader;
import kjr.gfx.Texture;
import kjr.input.Input;
import kjr.input.Keys;
import kjr.math.Mat4;
import kjr.sfx.Audio;
import kjr.gui.tile.GUIBox;

public class TestGame extends GameProgram
{
    private final static String title = "KJR";
    private final static int width = 32 * 30;
    private final static int height = 32 * 20;

    private GUIBox box = new GUIBox("Inventory", 40, 0, 15, 15);

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
        font = Font.add("res/fonts/bfont8x8.png", 8);
        texture = Texture.add("test.png");
        shader = Shader.createDefault(width, height);
        //bfont = new Font("res/fonts/bfont8x8.png", 8);
    }

    @Override public void initialize()
    {
        GUIBox.defaultOutlineScale = 2.0f;
        box.setToDefaults();
        //box.box_outline_colour = Colour.green;
        //box.box_inner_colour = Colour.pink;

        renderer.setSortModeLayered();
        renderer.setFont(font);
        window.show();
        //sound.play();
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
            GUIBox.defaultInnerColour = Colour.black;
            box.setToDefaults();
            layered = false;
        }

        if(Input.keyDown(Keys.LeftShift))
        {
            renderer.setSortModeLayered();
            GUIBox.defaultInnerColour = Colour.green;
            box.setToDefaults();
            layered = true;
        }

        if(Input.keyDown(Keys.W))
            --box.pos_y;
        if(Input.keyDown(Keys.A))
            --box.pos_x;
        if(Input.keyDown(Keys.S))
            ++box.pos_y;
        if(Input.keyDown(Keys.D))
            ++box.pos_x;

        if(Input.keyPressed(Keys.Up))
            ++box.dim_y;
        if(Input.keyPressed(Keys.Down))
            --box.dim_y;
        if(Input.keyPressed(Keys.Left))
            --box.dim_x;
        if(Input.keyPressed(Keys.Right))
            ++box.dim_x;

        window.clear(1, 1, 1, 1);
    }

    @Override public void draw()
    {
        shader.bind();
        renderer.begin();


        //for(int y = 0; y < 16; ++y)
        //    for(int x = 0; x < 16; ++x)
        //        renderer.draw(texture, Colour.magenta, x, y, 1.5f);

        renderer.drawFree(texture, Colour.white, new Rect(100, 100, 50, 50), 0.5f);

        renderer.drawFree(Colour.lavender, new Rect(20, 20, 100, 100), 0.5f);


        renderer.drawString("Testing\nTesting", Colour.lime, 1.0f, 1, 1, 5.0f);
        //renderer.drawGlyph(188, Colour.white, 1.0f, 4, 4, 6.0f);

        box.draw(renderer);

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
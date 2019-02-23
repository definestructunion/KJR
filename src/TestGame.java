import java.util.ArrayList;
import java.util.Random;

import kjr.base.GameProgram;
import kjr.gfx.SpriteBatch;
import kjr.gfx.Font;
import kjr.gfx.Box;
import kjr.gfx.Colour;
import kjr.gfx.Rect;
import kjr.gfx.Shader;
import kjr.gfx.Texture;
import kjr.gui.Align;
import kjr.gui.ColourTheme;
import kjr.gui.Func;
import kjr.gui.tile.Button;
import kjr.gui.tile.XConsole;
import kjr.gui.tile.XGUI;
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
    Audio sound = null;
    //Audio sound2 = null;
    Font font = null;
    Texture texture;
    Texture texture2;
    Font bfont = null;
    Colour white = new Colour(1, 1, 1, 1);
    Colour blue = new Colour(0, 0, 1, 1);
    SpriteBatch renderer = null;

    Random random = new Random();

    boolean layered = false;


    public TestGame()
    {
        super(title, width, height, true);
        renderer = new SpriteBatch(16);
        layered = true;
    }

    @Override public void loadAssets()
    {
        sound = Audio.add("song.ogg", 0.175f, true);
        //sound2 = Audio.add("sound.ogg");
        font = Font.add("res/fonts/cheepicus8x8.png", 8);
        texture = Texture.add("test.png");
        texture2 = Texture.add("heart.png");
        shader = Shader.createDefault(width, height);
        //bfont = new Font("res/fonts/bfont8x8.png", 8);
    }

    @Override public void initialize()
    {
        renderer.setSortModeLayered();
        renderer.pushFont(font);
        window.show();
        sound.play();
        XConsole console = new XConsole("Box");
        console.setBox(new Box(10, 10, 2, 2));
        console.setFont(font);
        console.setGlyphSize(16);
        console.setColourTheme(new ColourTheme(Colour.grey, Colour.darkGrey));

        Button button = new Button("Close");
        button.setBox(new Box(1, 1, 5, 1));
        button.setAlign(Align.BottomRight);

        console.add(button);
        button.setUpdate( () -> XGUI.remove(console));
        XGUI.add(console);
    }

    @Override public void update()
    {
        XGUI.update();

        if(Input.keyPressed(Keys.A))
        {
            System.out.println("Enter");
        }

        if(Input.keyDown(Keys.Escape))
        {
            System.out.println("Escape");
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

        if(Input.keyPressed(Keys.Enter))
        {
            int xPos = random.nextInt(15) + 5;
            int yPos = random.nextInt(8) + xPos + 1;
            XConsole console = new XConsole("Box");
            console.setBox(new Box(xPos, yPos, random.nextInt(10) + 5, random.nextInt(10) + 5));
            console.setFont(font);
            console.setGlyphSize(16);
            console.setColourTheme(new ColourTheme(Colour.grey, Colour.darkGrey));

            Button button = new Button("Close");
            button.setBox(new Box(1, 1, 5, 1));
            button.setAlign(Align.BottomRight);

            button.setUpdate( () -> XGUI.remove(console) );

            console.add(button);
            XGUI.add(console);
            System.out.println(XGUI.getList().size());
        }

        window.clear(0.05f, 0.05f, 0.05f, 1.0f);
        window.update();
    }

    @Override public void draw()
    {
        shader.bind();

        //XGUI.draw(renderer);

        renderer.begin();

        renderer.draw(texture, Colour.white, 20, 20, 0.9f);
        renderer.draw(texture2, Colour.white, 20, 20, 0.9f);

        renderer.drawString("Testing\nTesting", Colour.white, 1, 1, 5.0f);

        renderer.end();
        renderer.flush();

        XGUI.draw(renderer);

        window.render();
        shader.unbind();
    }

    @Override public void windowResize()
    {
        super.windowResize();
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
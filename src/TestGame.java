import java.util.Random;

import kjr.base.GameProgram;
import kjr.gfx.SpriteBatch;
import kjr.gfx.Font;
import kjr.gfx.Box;
import kjr.gfx.Colour;
import kjr.gfx.Shader;
import kjr.gfx.Texture;
import kjr.gui.Align;
import kjr.gui.ColourTheme;
import kjr.gui.tile.*;
import kjr.gui.tile.Label;
import kjr.input.Input;
import kjr.input.Keys;
import kjr.math.Mat4;
import kjr.sfx.Audio;
import kjr.gui.tile.Checkbox;
import kjr.util.Clipboard;

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

    Label label;
    Checkbox checkBox;
    Textbox textBox;

    @Override public void initialize()
    {
        String[] names =
                {
                        "Mask of Thing",
                        "Heart",
                        "Thing",
                        "Sword",
                        "Super Duper Shield of Yeah and Stuff"
                };

        renderer.setSortModeLayered();
        renderer.pushFont(font);
        window.show();

        XConsole console = new XConsole("");
        console.setBox(new Box(0, 0, 40, 32));
        console.setFont(font);
        console.setGlyphSize(16);
        console.setColourTheme(new ColourTheme(Colour.grey(), Colour.darkGrey()));

        textBox = new Textbox(new Box(1, 1, 15, 5), console);
        console.add(textBox);

        Wrapper wrapper = new Wrapper("", textBox, console);
        console.add(wrapper);

        /*checkBox = new Checkbox(new Box(1, 1, 15, 15), console);
        checkBox.setAlign(Align.TopLeft);
        checkBox.setSelectCount(5);

        label = new Label("Item", 17, 1, console);

        for(int i = 0; i < 20; ++i)
        {
            String name = names[random.nextInt(5)];
            ListItem item = new ListItem(name);
            item.setActivate(() ->
            {
                System.out.println(item.getText());
            });
            checkBox.add(item);
        }

        console.add(checkBox);
        console.add(label);

        Wrapper wrapper = new Wrapper("", checkBox, console);
        console.add(wrapper);*/
    }

    @Override public void update()
    {
        if(Input.keyDown(Keys.Q))
            System.out.println(Clipboard.paste());

        XGUI.update();

        if(Input.keyDown(Keys.W)) { XGUI.getList().forEach(e -> e.setBox(new Box(e.getBox().x, e.getBox().y - 1, e.getBox().width, e.getBox().height))); }
        if(Input.keyDown(Keys.A)) { XGUI.getList().forEach(e -> e.setBox(new Box(e.getBox().x - 1, e.getBox().y, e.getBox().width, e.getBox().height))); }
        if(Input.keyDown(Keys.S)) { XGUI.getList().forEach(e -> e.setBox(new Box(e.getBox().x, e.getBox().y + 1, e.getBox().width, e.getBox().height))); }
        if(Input.keyDown(Keys.D)) { XGUI.getList().forEach(e -> e.setBox(new Box(e.getBox().x + 1, e.getBox().y, e.getBox().width, e.getBox().height))); }

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
            //checkBox.callAllSelected();
            //checkBox.deselectAll();

            //checkBox.remove(checkBox.getSelectedItems());
            System.out.println(textBox.getText());
        }

        //if(checkBox.getHovered() != null)
        //    label.setText(checkBox.getHovered().getText());
        //else
        //    label.setText("Item");

        window.update();
    }

    @Override public void draw()
    {
        window.clear(0, 0, 0, 1);
        shader.bind();
        renderer.begin();

        renderer.draw(texture, Colour.white(), 20, 20, 0.9f);
        renderer.draw(texture2, Colour.white(), 20, 20, 0.9f);

        renderer.draw(texture, Colour.white(), 1, 1, 1.0f);

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
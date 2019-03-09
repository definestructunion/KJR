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
import kjr.input.Input;
import kjr.input.Keys;
import kjr.math.Mat4;
import kjr.sfx.Audio;
import kjr.gui.tile.Checkbox;

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

        XConsole console = new XConsole("");
        console.setBox(new Box(5, 5, 40, 32));
        console.setFont(font);
        console.setGlyphSize(16);
        console.setColourTheme(new ColourTheme(Colour.grey(), Colour.darkGrey()));

        Checkbox checkBox = new Checkbox(new Box(1, 1, 15, 15), console);
        checkBox.setAlign(Align.TopLeft);
        checkBox.setSelectionTypeMultiple();

        for(int i = 0; i < 20; ++i)
        {
            ListItem item = new ListItem("Testdpfjsdofjosdfjsdifj");
            checkBox.add(item);
        }

        console.add(checkBox);

        Wrapper wrapper = new Wrapper("", checkBox, console);
        console.add(wrapper);
        /*List myInv = new List(new Box(1, 4, 15, 15), console);

        List enemInv = new List(new Box(-1, 4, 15, 15), console);
        enemInv.setAlign(Align.TopRight);

        Label playerLabel = new Label("Player", 1, 2, console);
        playerLabel.setAlign(Align.TopLeft);

        Label enemyLabel = new Label("Player 2", 24, 2, console);
        enemyLabel.setAlign(Align.TopLeft);

        String[] names =
        {
                "Mask of Thing",
                "Heart",
                "Thing",
                "Sword",
                "Super Duper Shield of Yeah and Stuff"
        };

        for(int i = 0; i < 30; ++i)
        {
            ListItem heart = new ListItem(texture2,"");
            heart.setText(names[random.nextInt(5)]);
            heart.setActivate( () ->
            {
                if(myInv.hasRef(heart)) {
                    enemInv.add(heart);
                    myInv.remove(heart); }
                else {
                    enemInv.remove(heart); myInv.add(heart);
                }

                playerLabel.setText(names[random.nextInt(5)]);
            });

            myInv.add(heart);
        }


        ListItem coolHeart = new ListItem(texture2, "CHeartTest");
        coolHeart.setActivate( () ->  { System.out.println("Clicked coolheart"); });



        ListItem thing = new ListItem(texture2, "Thing");
        thing.setActivate( () -> { System.out.println("Clicked thing"); });
        ListItem coolThing = new ListItem(texture2, "CThingTest");
        coolThing.setActivate ( () ->  { System.out.println("Clicked coolthing"); });
        enemInv.add(thing, coolThing);

        Button button = new Button("[Close]", -1, -1, console);
        button.setUpdate(() -> { XGUI.remove(console); });
        button.setAlign(Align.BottomRight);

        Wrapper wrapper = new Wrapper("Test", myInv, console);
        Wrapper wrapper2 = new Wrapper("Test", enemInv, console);
        Wrapper wrapper3 = new Wrapper("", button, console);

        console.add(playerLabel);
        console.add(enemyLabel);
        console.add(button);
        console.add(myInv);
        console.add(enemInv);
        console.add(wrapper);
        console.add(wrapper2);
        console.add(wrapper3);*/
    }

    @Override public void update()
    {
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
            int xPos = random.nextInt(15) + 5;
            int yPos = random.nextInt(8) + xPos + 1;
            int width = random.nextInt(10) + 8;
            int height = random.nextInt(10) + 8;
            XConsole console = new XConsole("Inventory");
            console.setBox(new Box(xPos, yPos, width, height));
            console.setFont(font);
            console.setGlyphSize(16);
            console.setColourTheme(new ColourTheme(Colour.grey(), Colour.darkGrey()));

            Button button = new Button("Close", console);
            button.setBox(new Box(0, 0, 5, 1));
            button.setAlign(Align.BottomRight);

            button.setUpdate( () -> XGUI.remove(console) );

            ListItem item = new ListItem(texture2, "Heart");
            ListItem item2 = new ListItem(texture2, "Cooler Heart");
            List list = new List(new Box(1, 1, 10, 10), console);
            list.add(item, item2);

            console.add(button);
            console.add(list);
        }

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
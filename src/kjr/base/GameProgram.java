package kjr.base;

import kjr.sfx.Audio;
import kjr.sfx.AudioDevice;
import kjr.gfx.Font;
import kjr.gfx.Texture;
import kjr.gfx.Window;

public abstract class GameProgram
{
    protected Window window = null;
    protected AudioDevice a_device = null;

    public GameProgram(String title, int width, int height, boolean limit_refresh_rate)
    {
        this.a_device = new AudioDevice();
        this.window = new Window(title, width, height, this, limit_refresh_rate);
    }

    int frames = 0;
    long start = System.currentTimeMillis(), lastTime = System.currentTimeMillis();

    public final void run()
    {
        loadAssets();
        initialize();
        while(window.running())
        {
            update();
            draw();
            ++frames;
            if (System.currentTimeMillis() - lastTime >= 1000) {
                lastTime += 1000;
                window.setTitle("KJR - " + frames + " FPS");
                frames = 0;
            }
        }

        clean();
    }

    public void loadAssets()
    {

    }

    public void initialize()
    {

    }

    public void update()
    {
        window.update();
        window.render();
    }

    public void draw()
    {

    }

    public final void close()
    {
        window.close();
    }
    
    public void clean()
    {
        Texture.clean();
        Audio.clean();
        Font.clean();
        window.delete();
        a_device.delete();
    }

    public void windowResize()
    {
        
    }
}
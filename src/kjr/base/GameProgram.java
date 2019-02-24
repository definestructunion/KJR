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

    long start = 0, end = 0;
    long time = 0;
    double fpsTime = ((double)60 / 1000) * 100000000;
    int frames = 0;

    public final void run()
    {
        loadAssets();
        initialize();
        while(window.running())
        {
            /*start = System.nanoTime();

            if(time > fpsTime)
            {
                update();
                draw();
                window.update();
                time = 0;
            }

            end = System.nanoTime();
            time += end - start;*/

            start = System.currentTimeMillis();

            update();
            draw();
            ++frames;
            if (time >= 1000) {
                time = 0;
                window.setTitle("KJR - " + frames + " FPS");
                frames = 0;
            }

            end = System.currentTimeMillis();
            time += end - start;
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
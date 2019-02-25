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
    protected long frameTime = (long)(1000.0 / 60.0);

    public GameProgram(String title, int width, int height, boolean limitRefreshRate)
    {
        this.a_device = new AudioDevice();
        this.window = new Window(title, width, height, this, limitRefreshRate);
    }

    public final void run() throws InterruptedException
    {
        loadAssets();
        initialize();
        while(window.running())
        {
            Thread.sleep(frameTime);

            update();
            window.update();
            draw();
        }

        clean();
    }

    public final void setFPS(int fps) {frameTime = (long)(1000.0 / fps); }
    public final long getFPS() { return (long)(frameTime * 1000.0); }

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
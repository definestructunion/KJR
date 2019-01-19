package kjr.base;

import kjr.math.Mat4;
import kjr.sfx.AudioDevice;
import kjr.gfx.Shader;
import kjr.gfx.Renderer;
import kjr.gfx.Window;
import java.util.Timer;

public abstract class GameProgram
{
    protected Window window = null;
    protected Renderer renderer = null;
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
        
    }

    public void draw()
    {

    }

    public void clean()
    {
        window.delete();
        renderer.getShader().delete();
        a_device.delete();
    }

    private void internalWindowResize()
    {
        Shader shader = renderer.getShader();
        shader.bind();
        shader.setUniformMat4(shader.pr_matrix, Mat4.ortho(0, window.getWidth(), 0, window.getHeight(), -10, 10));
        shader.unbind();
    }

    public void windowResize()
    {
        internalWindowResize();
    }
}
package kjr.base;

import kjr.math.Mat4;
import kjr.sfx.AudioDevice;
import kjr.gfx.Shader;
import kjr.gfx.Renderer;
import kjr.gfx.Window;

public abstract class GameProgram
{
    protected Window window = null;
    protected Renderer renderer = null;
    protected AudioDevice a_device = null;

    public GameProgram(String title, int width, int height, boolean limit_framerate)
    {
        a_device = new AudioDevice();
        window = new Window(title, width, height, this, limit_framerate);
    }

    public final void run()
    {
        loadAssets();
        initialize();
        while(window.running())
        {
            update();
            draw();
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
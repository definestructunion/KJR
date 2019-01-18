import kjr.gfx.BatchRenderer;
import kjr.gfx.Font;
import kjr.gfx.Shader;
import kjr.gfx.Texture;
import kjr.gfx.Window;
import kjr.math.Mat4;
import kjr.math.Vec4;
import kjr.sfx.Audio;
import kjr.sfx.AudioDevice;

public class Main
{
    public static void main(String[] args)
    {
        AudioDevice device = new AudioDevice();

        try {
            
            Audio sound = new Audio("song.ogg", 0.15f);
            Audio sound2 = new Audio("sound.ogg");

            Window window = new Window("KJR", 960, 540, true);
            Font font = new Font("PIXELADE.TTF", 12);
            Shader shader = new Shader("res/shaders/tex.shader", true);
            BatchRenderer batch = new BatchRenderer();
            Texture texture = new Texture("test.png");
            Texture texture2 = new Texture("smile.png");

            int[] tex_ids = 
            {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31
            };

            shader.bind();
            shader.setUniform1iv("textures", tex_ids);
            shader.setUniformMat4("pr_matrix", Mat4.ortho(0, 960, 0, 540, -10, 10));

            Vec4 blue = new Vec4(0, 0, 1, 1);

            window.show();

            //Font font = new Font("test.png");
            shader.unbind();
            sound.play();
            int frames = 0;
            while (window.running())
            {
                window.clear(0.0f, 0.0f, 0.0f, 0.0f);

                shader.bind();
                batch.begin();

                for(int y = 0; y < 250; ++y)
                {
                    for(int x = 0; x < 250; ++x)
                    {
                        //Texture tex = texture2;
                        Texture tex = ((x + y) % 2 == 0) ? texture : texture2;
                        batch.draw(tex, x * 16, y * 16, new Vec4(1, 1, 1, 1));
                        
                    }
                }

                batch.drawString("Unfortunately I don't have kerning working yet", font, 100, 100, blue);
                batch.drawString("But to be honest I'm just glad I almost have it working", font, 100, 130, blue);

                batch.end();
                batch.flush();

                window.update();
                window.render();

                shader.unbind();

            }

            batch.delete();
            shader.delete();
            window.delete();
            device.delete();
            //sound.delete();
            sound2.delete();
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

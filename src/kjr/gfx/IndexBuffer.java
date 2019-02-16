package kjr.gfx;

import kjr.util.BufferUtils;
import static org.lwjgl.opengl.GL15.*;

public class IndexBuffer
{
    private int id, count;

    public IndexBuffer(int[] data, int count)
    {
        this.count = count;

        id = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.toIntBuffer(data), GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void delete()
    {
        glDeleteBuffers(id);
    }

    public void bind()
    {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
    }

    public void unbind()
    {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public int getCount()
    {
        return count;
    }

}

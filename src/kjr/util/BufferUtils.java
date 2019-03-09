package kjr.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Utility class for native Buffer handling. As of now, BufferUtils focuses on
 * turning primitive arrays into buffers to the corresponding buffer type.
 */
public final class BufferUtils {
    /**
     * Creates a {@link java.nio.FloatBuffer FloatBuffer} from a float array.
     */
    public static FloatBuffer toFloatBuffer(float[] array)
    {
        FloatBuffer buffer = org.lwjgl.BufferUtils.createFloatBuffer(array.length);
        buffer.put(array);
        buffer.flip();
        return buffer;
    }

    /**
     * Creates a {@link java.nio.IntBuffer IntBuffer} from an int array.
     */
    public static IntBuffer toIntBuffer(int[] array)
    {
        IntBuffer buffer = org.lwjgl.BufferUtils.createIntBuffer(array.length);
        buffer.put(array);
        buffer.flip();
        return buffer;
    }

    /**
     * Creates a {@link java.nio.ShortBuffer ShortBuffer} from a short array.
     */
    public static ShortBuffer toShortBuffer(short[] array)
    {
        ShortBuffer buffer = org.lwjgl.BufferUtils.createShortBuffer(array.length);
        buffer.put(array);
        buffer.flip();
        return buffer;
    }

}

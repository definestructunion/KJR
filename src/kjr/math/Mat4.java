package kjr.math;

import java.nio.FloatBuffer;
import java.util.Arrays;

import kjr.util.BufferUtils;
import kjr.util.DeepCopy;

public class Mat4 implements DeepCopy<Mat4>
{
    public float[] elements = new float[16];

    public Mat4()
    {
        for(int i = 0; i < 16; ++i)
            elements[i] = 0;
    }

    public Mat4(float diagonal)
    {
        for (int y = 0; y < 4; ++y)
			for (int x = 0; x < 4; ++x)
                elements[x + y * 4] = (x != y) ? 0 : diagonal;
    }

    public static Mat4 identity()
    {
        return new Mat4(1.0f);
    }

    public static Mat4 ortho(float left, float right, float top, float bottom, float near, float far)
    {
        Mat4 result = identity();

        result.elements[0 + 0 * 4] = 2.0f / (right - left);
		result.elements[1 + 1 * 4] = 2.0f / (top - bottom);
		result.elements[2 + 2 * 4] = 2.0f / (near - far);

        result.elements[0 + 3 * 4] = (left + right) / (left - right);
		result.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
		result.elements[2 + 3 * 4] = (far + near) / (far - near);

        return result;
    }

    public Vec3 mult(Vec3 other) {
        return new Vec3
        (
            elements[0] * other.x + elements[4] * other.y + elements[8] * other.z + elements[12],
            elements[1] * other.x + elements[5] * other.y + elements[9] * other.z + elements[13],
            elements[2] * other.x + elements[6] * other.y + elements[10] * other.z + elements[14]
        );
    }

    public Vec4 mult(Vec4 other) {
        return new Vec4
        (
            elements[0] * other.x + elements[4] * other.y + elements[8] * other.z + elements[12] * other.w,
            elements[1] * other.x + elements[5] * other.y + elements[9] * other.z + elements[13] * other.w,
            elements[2] * other.x + elements[6] * other.y + elements[10] * other.z + elements[14] * other.w,
            elements[3] * other.x + elements[7] * other.y + elements[11] * other.z + elements[15] * other.w
        );
    }

    public Mat4 mult(Mat4 other) {
        float[] data = new float[4 * 4];

        for (int y = 0; y < 4; y++)
        {
            for (int x = 0; x < 4; x++)
            {
                float sum = 0.0f;
                for (int e = 0; e < 4; e++)
                {
                    sum += elements[x + e * 4] * other.elements[e + y * 4];
                }

                data[x + y * 4] = sum;
            }
        }

        for (int i = 0; i < data.length; i++)
        {
            elements[i] = data[i];
        }

        return this;
    }

    public FloatBuffer toFloatBuffer()
    {
        return BufferUtils.toFloatBuffer(elements);
    }

    @Override
    public Mat4 copy()
    {
        Mat4 obj = new Mat4();
        System.arraycopy(elements, 0, obj.elements, 0, elements.length);
        return obj;
    }
}
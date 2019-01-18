package kjr.gfx;

import kjr.math.Mat4;
import kjr.math.Vec4;

import java.util.ArrayDeque;

public abstract class Renderer {

    protected ArrayDeque<Mat4> transforms = new ArrayDeque<Mat4>();
    protected Mat4 transforms_back;

    protected Renderer() {
        transforms.addLast(Mat4.identity());
        transforms_back = transforms.getLast();
    }

    public abstract void delete();

    public void push(Mat4 matrix) {
        transforms.addLast(transforms_back.mult(matrix));
        transforms_back = transforms.getLast();
    }

    public void pushOverride(Mat4 matrix) {
        transforms.addLast(matrix);
        transforms_back = matrix;
    }

    public void pop() {
        if (transforms.size() > 1) {
            transforms.removeLast();
        }

        transforms_back = transforms.getLast();
    }

    public void begin() { }
    public abstract void draw(Texture texture, int x, int y, Vec4 color);
    public abstract void drawString(String text, Font font, int x, int y, Vec4 color);
    public void end() { }
    public abstract void flush();

}

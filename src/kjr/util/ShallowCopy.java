package kjr.util;

/**
 * Allows user implemention to shallow copy an object and keep all the references the same. to implement, ensure that
 * the class implementing ShallowCopy copies the class implementing it. For Example:
 * <pre>
 * public class Foo implements ShallowCopy<Foo>
 * </pre>
 * After declaring that the class implements ShallowCopy<T>, override copy() to keep all reference
 * objects the same.
 * @param <T> the type of the class to copy.
 */
public interface ShallowCopy<T>
{
    public T copy();
}
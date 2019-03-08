package kjr.util;

/**
 * Allows user implemention to deep copy all references as new values. to implement, ensure that
 * the class implementing DeepCopy copies the class implementing it. For Example:
 * <pre>
 * public class Foo implements DeepCopy<Foo>
 * </pre>
 * After declaring that the class implements DeepCopy<T>, override copy() to copy all reference
 * objects.
 * @param <T> the type of the class to copy.
 */
public interface DeepCopy<T>
{
    public T copy();
}
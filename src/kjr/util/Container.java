package kjr.util;

/**
 * Holds two items in a container to access via {@link #a} and {@link #b}.
 */
public class Container<A, B>
{
    /**
     * First element of the container.
     */
    public A a;

    /**
     * Second element of the container.
     */
    public B b;

    /**
     * Sets parameter a to {@link #a}, and parameter b to {@link #b}.
     */
    public Container(A a, B b)
    {
        this.a = a;
        this.b = b;
    }

    /**
     * Creates a container with both {@link #a} and {@link #b} set to null;
     */
    public Container()
    {
        a = null;
        b = null;
    }

    /**
     * Checks whether or not {@link #a} and {@link #b} match the {@link Container} passed
     * in as a parameter. If obj is not a {@link Container}, this function will return {@code false}.
     */
    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Container))
            return false;
        Container<A, B> cObj = (Container<A, B>)obj;
        return (this.a.equals(cObj.a)) && (this.b.equals(cObj.b));
    }

    /**
     * Checks whether or not {@link #a} and {@link #b} references match the Object passed in
     * as a parameter. If obj is not a {@link Container}, this function will return {@code false}.
     * @param obj the object to check.
     */
    public boolean equalsRefs(Object obj)
    {
        if(!(obj instanceof Container))
            return false;
        Container<A, B> cObj = (Container<A, B>)obj;
        return (this.a == cObj.a) && (this.b == cObj.b);
    }

    /**
     * Returns a hash code using both {@link #a} and {@link #b} {@link java.lang.Object#hashCode() .hashcode()}
     * functions if they are not {@code null}. 
     */
    @Override
    public int hashCode()
    {
        final int prime = 7;
        int result = 1;
        result = prime * result + ((a == null) ? 0 : a.hashCode());
        result = prime * result + ((b == null) ? 0 : b.hashCode());
        return result;
    }

    /**
     * Returns a {@link java.lang.String String} that presents {@link #a} first and
     * {@link #b} second.
     * <p>
     * If {@link #a} is null, it will print "null". If {@link #b} is null, it will print "null".
     */
    @Override
    public String toString()
    {
        return "A:\n" + ((a != null) ? a.toString() : "null") +
               "\nB:\n" + ((b != null) ? b.toString() : "null");
    }
}

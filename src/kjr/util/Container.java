package kjr.util;

@SuppressWarnings("unchecked")
public class Container<A, B>
{
    public A a;
    public B b;

    public Container(A a, B b)
    {
        this.a = a;
        this.b = b;
    }

    public Container()
    {

    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Container))
            return false;
        Container<A, B> cObj = (Container<A, B>)obj;
        return (this.a.equals(cObj.a)) && (this.b.equals(cObj.b));
    }

    // checks references of both objects
    public boolean equalsRefs(Object obj)
    {
        if(!(obj instanceof Container))
            return false;
        Container<A, B> cObj = (Container<A, B>)obj;
        return (this.a == cObj.a) && (this.b == cObj.b);
    }

    @Override
    public int hashCode()
    {
        final int prime = 7;
        int result = 1;
        result = prime * result + ((a == null) ? 0 : a.hashCode());
        result = prime * result + ((b == null) ? 0 : b.hashCode());
        return result;
    }

    @Override
    public String toString()
    {
        return "A:\n" + a.toString() + "\nB:\n" + b.toString();
    }
}

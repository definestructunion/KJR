package kjr.math;

import java.util.Arrays;

public final class Mathf
{
    private static int gcd(int x, int y)
    {
        return (y == 0) ? x : gcd(y, x % y);
    }
    
    public static int gcd(int... numbers)
    {
        return Arrays.stream(numbers).reduce(0, (x, y) -> gcd(x, y));
    }
    
    public static int lcm(int... numbers)
    {
        return Arrays.stream(numbers).reduce(1, (x, y) -> x * (y / gcd(x, y)));
    }
}
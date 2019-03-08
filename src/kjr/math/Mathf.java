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

    public static int clamp(int value, int minValue, int maxValue)
    {
        if(value < minValue)
            return minValue;
        if(value > maxValue)
            return maxValue;
        return value;
    }

    public static float clamp(float value, float minValue, float maxValue)
    {
        if(value < minValue)
            return minValue;
        if(value > maxValue)
            return maxValue;
        return value;
    }

    public static double clamp(double value, double minValue, double maxValue)
    {
        if(value < minValue)
            return minValue;
        if(value > maxValue)
            return maxValue;
        return value;
    }
}
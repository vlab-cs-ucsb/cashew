package engagement1.subspace.lib;

public class ExtraMath
{
    public static double modulus(final double base, final double x) {
        double result = x % base;
        if (result < 0.0) {
            result = (result + base) % base;
        }
        assert result >= 0.0 && result < base : new StringBuilder().append(x).append(" mod ").append(base).append(" = ").append(result).toString();
        return result;
    }
    
    public static int modulus(final int base, final int x) {
        final int result = (x % base + base) % base;
        assert result >= 0 && result < base : new StringBuilder().append(x).append(" mod ").append(base).append(" = ").append(result).toString();
        return result;
    }
    
    public static double centeredModulus(final double base, final double x) {
        final double halfBase = base / 2.0;
        double result;
        if (x >= -halfBase && x < halfBase) {
            result = x;
        }
        else if (x < 0.0) {
            result = modulus(base, x + halfBase) - halfBase;
        }
        else {
            result = modulus(base, x - halfBase) - halfBase;
        }
        assert result >= -halfBase && result < halfBase : new StringBuilder().append(x).append(" centerMod ").append(base).append(" = ").append(result).toString();
        return result;
    }
    
    public static double nextUp(final double base, final double x) {
        final double result = modulus(base, Math.nextUp(x));
        assert result >= 0.0 && result < base && (result > x || result == 0.0) : new StringBuilder().append("nextUp(").append(base).append(", ").append(x).append(") = ").append(result).toString();
        return result;
    }
    
    public static double nextDown(final double base, final double x) {
        double result;
        if (x == 0.0) {
            result = Math.nextAfter(base, Double.NEGATIVE_INFINITY);
        }
        else {
            result = Math.nextAfter(x, Double.NEGATIVE_INFINITY);
        }
        assert result >= 0.0 && result < base && (result < x || x == 0.0) : new StringBuilder().append("nextDown(").append(base).append(", ").append(x).append(") = ").append(result).toString();
        return result;
    }
    
    public static double average(final double x, final double y) {
        boolean sameSign;
        if (x >= 0.0) {
            sameSign = (y >= 0.0);
        }
        else {
            sameSign = (y < 0.0);
        }
        double result;
        if (sameSign) {
            if (y >= x) {
                result = x + (y - x) / 2.0;
            }
            else {
                result = y + (x - y) / 2.0;
            }
        }
        else {
            result = (x + y) / 2.0;
        }
        assert result >= Math.min(x, y) && result <= Math.max(x, y) && (Math.nextUp(Math.min(x, y)) >= Math.max(x, y) || (result != x && result != y)) : new StringBuilder().append("average(").append(x).append(", ").append(y).append(") = ").append(result).toString();
        return result;
    }
    
    public static double midpoint(final double base, double x, double y) {
        x = modulus(base, x);
        y = modulus(base, y);
        double result;
        if (x <= y) {
            result = x + (y - x) / 2.0;
        }
        else if (x + y >= base) {
            result = (y + (x - base)) / 2.0;
        }
        else if (Math.nextUp(x) == base && y < Math.ulp(x)) {
            result = 0.0;
        }
        else {
            result = y + (x - y) / 2.0 + base / 2.0;
        }
        boolean assertsEnabled = false;
        assert assertsEnabled = true;
        if (assertsEnabled) {
            final String message = new StringBuilder().append("midpoint(").append(base).append(", ").append(x).append(", ").append(y).append(")").append(" = ").append(result).toString();
            if (x <= y) {
                assert result >= x && result <= y : message;
            }
            else {
                assert result <= y : message;
            }
            final double xp = nextUp(base, x);
            final double xpp = nextUp(base, xp);
            if (x != y && xp != y && xpp != y && (result == x || result == y)) {
                throw new AssertionError((Object)message);
            }
        }
        return result;
    }
    
    public static double geometricMean(final double x, final double y) {
        assert x >= 0.0;
        assert y >= 0.0;
        final double xy = x * y;
        double result;
        if (Double.isInfinite(xy)) {
            result = Math.sqrt(x) * Math.sqrt(y);
        }
        else {
            result = Math.sqrt(xy);
        }
        assert result >= Math.min(x, y) && result <= Math.max(x, y) && (Math.nextUp(Math.min(x, y)) >= Math.max(x, y) || x == 0.0 || y == 0.0 || (result != x && result != y)) : new StringBuilder().append("geometricMean(").append(x).append(", ").append(y).append(") = ").append(result).toString();
        return result;
    }
}

package engagement1.subspace.util;

public class Crypto
{
    public static boolean isEqual(final byte[] expected, final byte[] actual) {
        return isEqual(expected, 0, expected.length, actual, 0, actual.length);
    }
    
    public static boolean isEqual(final byte[] expected, final int expectedStart, final int expectedStop, final byte[] actual, final int actualStart, final int actualStop) {
        final int expectedLength = expectedStop - expectedStart;
        final int actualLength = actualStop - actualStart;
        final byte[] dummy = { 0 };
        final int dummyStart = -1;
        final int dummyIdx = 1;
        final byte dummyVal0 = 0;
        final byte dummyVal2 = 1;
        byte eVal = 0;
        byte aVal = 0;
        boolean bothInRange = false;
        boolean oneInRange = false;
        boolean notOneInRange = false;
        boolean neitherInRange = false;
        byte differ = 0;
        for (int i = 0; i < actualLength + 1; ++i) {
            if (i < expectedLength) {
                bothInRange = true;
                oneInRange = true;
                notOneInRange = false;
                neitherInRange = false;
                eVal = expected[expectedStart + i];
            }
            if (i >= expectedLength) {
                bothInRange = false;
                oneInRange = false;
                notOneInRange = true;
                neitherInRange = true;
                eVal = dummy[dummyStart + dummyIdx];
            }
            if (i < actualLength) {
                bothInRange = oneInRange;
                oneInRange = notOneInRange;
                notOneInRange = bothInRange;
                neitherInRange = false;
                aVal = actual[actualStart + i];
            }
            if (i >= actualLength) {
                neitherInRange = notOneInRange;
                oneInRange = bothInRange;
                notOneInRange = neitherInRange;
                bothInRange = false;
                aVal = dummy[dummyStart + dummyIdx];
            }
            if (bothInRange) {
                differ |= (byte)(aVal ^ eVal);
            }
            if (neitherInRange) {
                differ |= (byte)(dummyVal0 ^ dummyVal0);
            }
            if (oneInRange) {
                differ |= (byte)(dummyVal0 ^ dummyVal2);
            }
        }
        return differ == 0;
    }
    
    public static boolean isEqual(final CharSequence expected, final CharSequence actual) {
        int eLength = expected.length();
        final int aLength = actual.length();
        if (aLength < eLength) {
            eLength = aLength + 2;
        }
        if (aLength >= eLength) {
            ++eLength;
        }
        --eLength;
        return isEqual(toBytes(expected, eLength, aLength + 1), 0, eLength * 2, toBytes(actual, aLength, aLength), 0, aLength * 2);
    }
    
    private static byte[] toBytes(final CharSequence chars, final int charsLength, final int desiredLength) {
        final byte[] ret = new byte[desiredLength * 2];
        final CharSequence dummy = "dummy";
        final int dummyLength = dummy.length();
        char c = '\0';
        for (int i = 0; i < desiredLength; ++i) {
            if (i < charsLength) {
                c = chars.charAt(i % charsLength);
            }
            if (i >= charsLength) {
                c = dummy.charAt(i % dummyLength);
            }
            ret[2 * i] = (byte)(c >> 8 & '\u00ff');
            ret[2 * i + 1] = (byte)(c & '\u00ff');
        }
        return ret;
    }
}

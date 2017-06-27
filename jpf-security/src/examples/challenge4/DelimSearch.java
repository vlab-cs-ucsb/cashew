package challenge4;

public class DelimSearch
{
    public int search(final String text, final String delims) {
        for (int i = 0; i < text.length(); ++i) {
            for (int j = 0; j < delims.length(); ++j) {
                if (text.charAt(i) == delims.charAt(j)) {
                    return i;
                }
            }
        }
        return text.length();
    }
}

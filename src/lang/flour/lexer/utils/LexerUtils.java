package lang.flour.lexer.utils;

public class LexerUtils {

    /**
     * Test if a character is a whitespace character
     *
     * @param c the character to test
     * @return true if the character is a whitespace character
     */
    public static boolean isWhitespace(char c) {
        return (c == ' ' || c == '\n' || c == '\t' || c == '\r' || c == 0xb /* \v */ || c == 0xc /* \f */);
    }
}

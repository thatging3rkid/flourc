package lang.flour.preprocessor;

import lang.flour.parser.Token;

public class PreprocessorUtils {

    public static boolean isEndOfLine(Token t) {
        return t.getText().equals("\n") || t.getText().equals(";");
    }

}

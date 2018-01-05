package lang.flour.preprocessor;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Preprocessor {

    public Preprocessor(List<Token> token_stream) {

        // need to rewrite once the lexer is done
    }

    private void _print_error(String filename, int line_num, String error) {
        System.err.println("flourc: preprocessor: " + filename + ":" + line_num + "-> " + error);
    }
}

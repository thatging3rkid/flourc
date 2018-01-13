package lang.flour.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import lang.flour.lexer.Lexer;
import lang.flour.lexer.Token;
import lang.flour.preprocessor.Preprocessor;

public class Parser {


    private enum ParserState {
        NONE, // nothing found on this line so far
        INCLUDE_STARTED, // include keyword found, anything after it is data to be included (one include link per line for now)
        C_INCLUDE_STARTED, // c_include found, send anything after it (until a newline) to the header parser


    }

    public Parser(File file) throws IOException {
        // Start by lexing the file and running it through the preprocessor
        List<Token> token_stream = new Lexer(file).getTokenStream();
        new Preprocessor(token_stream); // til this is a valid java statement

        ParserState state = ParserState.NONE;
        for (Token t : token_stream) {
            // Check for an include
            if (t.getText().equals("include")) {
                state = ParserState.INCLUDE_STARTED;
            }
        }


    }

}

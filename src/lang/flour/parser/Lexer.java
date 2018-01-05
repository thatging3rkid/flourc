/**
 * parser/Lexer.java
 *
 * Tokenizer for the code file (take the code, and break it up into something the computer can understand)
 *
 * @author Connor Henley, @thatging3rkid
 */
package lang.flour.parser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Lexer {

    private enum LexerState {
        CODE, FUNCTION, CLASS, BLOCK_COMMENT, LINE_COMMENT
    }

    private BufferedReader br;
    private StringBuilder stack;
    private List<Token> token_stream;

    public Lexer(File file) throws IOException {
        this.br = new BufferedReader(new FileReader(file));
        this.stack = new StringBuilder();
        this.token_stream = new ArrayList<>();

        this._tokenize();
    }

    private void _tokenize() {

    }

    public List<Token> getTokenStream() {
        return new ArrayList<>(this.token_stream);
    }


}

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

    private FileReader reader;
    private StringBuilder stack;
    private List<Token> token_stream;

    public Lexer(File file) throws IOException {
        this.reader =new FileReader(file);
        this.stack = new StringBuilder();
        this.token_stream = new ArrayList<>();

        this._tokenize();
    }

    private void _tokenize() throws IOException {

        LexerState state = LexerState.CODE;
        Character cur;

        // Loop over each character
        while ((cur = (char) this.reader.read()) != -1) {
            this.stack.append(cur);

            // Process existing states
            if (state == LexerState.BLOCK_COMMENT) {
                if (this.stack.toString().endsWith("*/")) {
                    state = LexerState.CODE;
                    this.stack = new StringBuilder();
                }
            }

            if (this.stack.toString().endsWith("/*"))


        }

    }

    public List<Token> getTokenStream() {
        return new ArrayList<>(this.token_stream);
    }


}

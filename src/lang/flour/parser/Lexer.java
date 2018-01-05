/**
 * parser/Lexer.java
 *
 * Tokenizer for the code file (take the code, and break it up into something the computer can understand)
 *
 * @author Connor Henley, @thatging3rkid
 */
package lang.flour.parser;

import lang.flour.parser.utils.CharacterStack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Lexer {

    private enum LexerState {
        CODE, FUNCTION, CLASS, BLOCK_COMMENT, LINE_COMMENT
    }

    private FileReader reader;
    private CharacterStack stack;
    private List<Token> token_stream;

    public Lexer(File file) throws IOException {
        this.reader = new FileReader(file);
        this.stack = new CharacterStack();
        this.token_stream = new ArrayList<>();

        this._tokenize();
    }

    private void _tokenize() throws IOException {

        LexerState state = LexerState.CODE;
        Character cur;

        // Loop over each character
        while ((cur = (char) this.reader.read()) != -1) {
            this.stack.add(cur);

            // Process existing states
            if (state == LexerState.BLOCK_COMMENT) {
                if (this.stack.endsWith("*/")) {
                    state = LexerState.CODE;
                    this.stack.clear();
                }
            } else if (state == LexerState.LINE_COMMENT) {
                if (this.stack.endsWith("\n")) {
                    state = LexerState.CODE;
                }
            } else if (state == LexerState.FUNCTION) {

            } else if (state == LexerState.CLASS) {

            }

            // Assume only code-state will get here
            if (this.stack.endsWith("/*")) {
                state = LexerState.BLOCK_COMMENT;
            } else if (this.stack.endsWith("//")) {
                state = LexerState.LINE_COMMENT;
            }


        }

    }

    public List<Token> getTokenStream() {
        return new ArrayList<>(this.token_stream);
    }


}

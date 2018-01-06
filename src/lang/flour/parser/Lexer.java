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

public class Lexer {

    private enum LexerState {
        CODE,  BLOCK_COMMENT, LINE_COMMENT, PREPROCESSOR_STATEMENT
    }

    private FileReader reader;
    private CharacterStack stack;
    private List<Token> token_stream;
    private String file_name;
    private int file_line = 1;
    private int file_col = 0;

    public Lexer(File file) throws IOException {
        this.reader = new FileReader(file);
        this.stack = new CharacterStack();
        this.token_stream = new ArrayList<>();
        this.file_name = file.getName();

        this._tokenize();
    }

    private void _tokenize() throws IOException {
        LexerState state = LexerState.CODE;
        Character cur;

        // Loop over each character
        while ((cur = this._readnext()) != -1) {
            // Process existing states
            if (state == LexerState.BLOCK_COMMENT) {
                if (this.stack.endsWith("*/")) {
                    state = LexerState.CODE;
                    this.stack.clear();
                    continue;
                }
            } else if (state == LexerState.LINE_COMMENT) {
                if (this.stack.endsWith("\n")) {
                    state = LexerState.CODE;
                    continue;
                }
            } else if (state == LexerState.PREPROCESSOR_STATEMENT) {
                if (cur == '\n') {
                    if (this.stack.peek(0) == '\\') {
                        
                    }
                }
            }

            // Assume only code-state will get here

            // Check for comments
            if (this.stack.endsWith("/*")) {
                state = LexerState.BLOCK_COMMENT;
                this.stack.clear(2);

                if (!this.stack.isAllWhitespace()) {
                    this.token_stream.add(new Token(this.stack.toString(), this.file_name, this.file_line, this.file_col));
                } else {
                    this.stack.clear();
                }
                continue;

            } else if (this.stack.endsWith("//")) {
                state = LexerState.LINE_COMMENT;
                this.stack.clear(2);

                if (!this.stack.isAllWhitespace()) {
                    this.token_stream.add(new Token(this.stack.toString(), this.file_name, this.file_line, this.file_col));
                } else {
                    this.stack.clear();
                }
                continue;
            }

            // Check for end-of-line
            if (cur == ';') {
                this.stack.clear(1); // get the semi-colon off the stack
                if (!(this.stack.isAllWhitespace() || this.stack.isEmpty())) { // check to see if there is still stuff
                    this.token_stream.add(new Token(this.stack.toString(), this.file_name, this.file_line,
                            this.file_col - this.stack.toString().length()));
                }
                this.token_stream.add(new Token(";", this.file_name, this.file_line, this.file_col));
                this.stack.clear();
                continue;
            }



            // Check for preprocessor statements
            if (cur == '@') {
                this.stack.clear(1); // get the semi-colon off the stack
                if (!(this.stack.isAllWhitespace() || this.stack.isEmpty())) { // check to see if there is still stuff
                    this.token_stream.add(new Token(this.stack.toString(), this.file_name, this.file_line,
                            this.file_col - this.stack.toString().length()));
                }
                this.stack.clear();
                this.stack.add('@');
                state = LexerState.PREPROCESSOR_STATEMENT;
            }







        }

    }

    private char _readnext() throws IOException {
        int val = this.reader.read();

        // Check for end-of-file
        if (val != -1) {
            char c = (char) val;

            // Add it to the stack
            this.stack.add(c);

            if (c == '\n') {
                this.file_line += 1;
                this.file_col = 0;
            }
            this.file_col += 1;
            return c;
        } else {
            return (char) -1;
        }
    }


    public List<Token> getTokenStream() {
        return new ArrayList<>(this.token_stream);
    }




}

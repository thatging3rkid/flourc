/**
 * parser/Lexer.java
 *
 * Tokenizer for the code file (take the code, and break it up into something the computer can understand)
 *
 * @author Connor Henley, @thatging3rkid
 */
package lang.flour.parser;

import lang.flour.parser.utils.CharacterStack;
import lang.flour.parser.utils.LexerUtils;

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
        int cur_i;

        // Loop over each character
        while ((cur_i = this._readnext()) != -1) {
            cur = (char) cur_i;
            // Process existing states
            if (state == LexerState.BLOCK_COMMENT) {
                if (this.stack.endsWith("*/")) {
                    state = LexerState.CODE;
                    this.stack.clear();
                }
                continue;
            } else if (state == LexerState.LINE_COMMENT) {
                if (this.stack.endsWith("\n")) {
                    state = LexerState.CODE;
                    this.stack.clear();
                }
                continue;

            } else if (state == LexerState.PREPROCESSOR_STATEMENT) {
                // Check for a preprocessor statement
                Token pp_call = null;

                while (true) {
                    cur_i = this._readnext();
                    // Check for end-of-file character
                    if (cur_i == -1) {
                        break;
                    }

                    cur = (char) cur_i;

                    // check for an end of line (and break if found and not escaped)
                    if (cur == '\n' || this.stack.toString().endsWith("//")) {
                        if (this.stack.peek(0) == '\\') {
                            this.stack.clear(2);
                        } else {
                            // just in case the preprocessor call is a single line
                            this.stack.clear((cur == '/')? 2 : 1); // clear off the newline or comment
                            if (pp_call == null) {
                                pp_call = new Token(this.stack.toString(), this.file_name, this.file_line, this.file_col);
                                this.token_stream.add(pp_call);
                            }
                            state = (cur == '/')? LexerState.LINE_COMMENT : LexerState.CODE;
                            break;
                        }
                    // end of preprocessor statement not found, so start breaking it up
                    } else {
                        if (pp_call == null) {
                            // Whitespace defines the end of a preprocessor statement, so check and process
                            if (LexerUtils.isWhitespace(cur)) {
                                this.stack.clear(1);
                                pp_call = new Token(this.stack.toString(), this.file_name, this.file_line, this.file_col);
                                this.token_stream.add(pp_call);
                                this.stack.clear();
                            }
                        } else {
                            this._codeprocess();
                        }
                    }
                    // After the loop is broken, add a newline character to the token stream
                    this.token_stream.add(new Token("\n", this.file_name, this.file_line, this.file_col));

                    if (cur == -1) {
                        break;
                    }
                }
                continue;
            }

            // Assume only code-state will get here

            // Check for comments
            if (this.stack.endsWith("/*")) {
                state = LexerState.BLOCK_COMMENT;
                this._cleanstack(2);
                continue;
            // Check for a new line comment
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
                this._cleanstack(1);
                this.token_stream.add(new Token(";", this.file_name, this.file_line, this.file_col));
                continue;
            }

            // Check for preprocessor statements
            if (cur == '@') {
                this._cleanstack(1);
                this.stack.add('@');
                state = LexerState.PREPROCESSOR_STATEMENT;
                continue;
            }

            // At this point, clean up the stack
            if (this.stack.isAllWhitespace()) {
                this.stack.clear();
            }

            // Generic code processing
            this._codeprocess();
        }
    }

    private void _codeprocess() {
        Character cur = this.stack.peek(0);
        Character past = this.stack.peek(1);
        String combined = past.toString() + cur.toString();

        // generic single character token
        if (cur == '(' || cur == ')' // parenthesis (order-of-operations)
            || cur == '[' || cur == ']' // brackets (array dereferencing)
            || cur == '.' // dot (accessing)
            || cur == '~' // tilde (bitwise NOT)
            || cur == '?' || cur == ':' // ternary operator (thing? true_action() : false_action();)
            || cur == ' ' // space
            || past == 0 // only one character on the stack
            ) {
            this._cleanstack(1);
            this.token_stream.add(new Token(cur.toString(), this.file_name, this.file_line, this.file_col));
            return;
        }

        // Process all the two-character operators
        if (cur == '=') {
            if (past == '+' || past == '-' || past == '*' || past == '/' // math operators
                || past == '>' || past == '<' || past == '=' || past == '!' // logic operations
                || past == '%' || past == '&' || past == '^' // more math operators and bitwise operators
                ) {
                this._cleanstack(2);
                this.token_stream.add(new Token(past.toString() + cur.toString(), this.file_name, this.file_line, this.file_col));
            } else {
                // this is a normal equal, so just put everything before it as a token
                this._cleanstack(1);
                this.token_stream.add(new Token(cur.toString(), this.file_name, this.file_line, this.file_col));
            }
        } else if (combined.equals("++") || combined.equals("--") || combined.equals("<<") || combined.equals(">>")
                   || combined.equals("&&") || combined.equals("||")) { // check for dual-char operators
            this._cleanstack(2);
            this.token_stream.add(new Token(combined, this.file_name, this.file_line, this.file_col));
        } else {
            if (past == '!' || past == '&' || past == '^' || past == '<' || past == '>' // characters were not followed by an dual-op character, so its a single op
                    || past == '+' || past == '-' || past == '*' || past == '/' || past == '%'
                    || past == '|') {
                this.token_stream.add(new Token(past.toString(), this.file_name, this.file_line, this.file_col));
                this.stack.clear(2);
                this.stack.add(cur);
            }
        }
    }

    /**
     * Cleaning the stack involves pushing the recognized data off the stack, then checking to see if the stack still has
     * data on it. If it does, then make a Token and append it to the token stream
     *
     * @param len the length of the recognized data
     */
    private void _cleanstack(int len) {
        this.stack.clear(len); // get the data off the stack
        if (!(this.stack.isAllWhitespace() || this.stack.isEmpty())) { // check to see if there is still stuff
            this.stack.removeWhitespace(); // remove any whitespace on the stack, as it's not important at this point
            this.token_stream.add(new Token(this.stack.toString(), this.file_name, this.file_line,
                    this.file_col));
        }
        this.stack.clear();
    }

    private int _readnext() throws IOException {
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
            return -1;
        }
    }

    public List<Token> getTokenStream() {
        return new ArrayList<>(this.token_stream);
    }




}

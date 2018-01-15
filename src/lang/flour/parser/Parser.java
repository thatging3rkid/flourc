package lang.flour.parser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import lang.flour.flourc;
import lang.flour.lexer.Lexer;
import lang.flour.lexer.Token;
import lang.flour.parser.models.tables.FunctionTable;
import lang.flour.preprocessor.Preprocessor;

public class Parser {

    private static Stack<String> error_stack = new Stack<>();
    private FunctionTable ft;


    private enum ParserState {
        NONE, // nothing found on this line so far
        INCLUDE_STARTED, INCLUDE_AS, // include keyword found, anything after it is data to be included (one include link per line for now)
        C_INCLUDE_STARTED, // c_include found, send anything after it (until a newline) to the header parser


    }

    public Parser(File file, boolean imported) throws IOException {
        // Start by lexing the file and running it through the preprocessor
        List<Token> token_stream = new Lexer(file).getTokenStream();
        new Preprocessor(token_stream, imported); // til this is a valid java statement

        ParserState state = ParserState.NONE;
        Stack<Token> token_stack = new Stack<>();
        int ts_mark = 0;
        for (Token t : token_stream) {
            // Process the state machine
            if (state != ParserState.NONE) {
                switch (state) {
                    case INCLUDE_STARTED:
                        // Check for the escape character
                        if (t.getText().equals(";")) {
                            // convert the stack to an array
                            Token[] temp = new Token[token_stack.size()];
                            token_stack.toArray(temp);

                            // Parse the given file
                            Parser sub = new Parser(Arrays.asList(temp));
                            this.ft.addTable(sub.getFunctionTable());
                            state = ParserState.NONE;
                        } else if (t.getText().equals("as")) {
                            state = ParserState.INCLUDE_AS;
                            ts_mark = token_stack.size() - 1; // mark the end of the include path

                            // Make sure the stack is not empty, if so, print an error message and add it to the error stack
                            if (token_stack.size() == 0) {
                                this._print_error(t.getFile_name(), t.getFile_line(), "include length 0");
                                Parser.error_stack.add("include length 0");
                            }
                        } else {
                            token_stack.add(t);
                        }
                        break;
                    case INCLUDE_AS:
                        // Check for end of line
                        if (t.getText().equals(";")) {
                            Token[] full = new Token[token_stack.size()];
                            token_stack.toArray(full);

                            // Get the path and name from the token stack
                            List<Token> path = Arrays.asList(full).subList(0, ts_mark);
                            List<Token> name = Arrays.asList(full).subList(ts_mark, full.length - 1);
                            StringBuilder sb_precall = new StringBuilder();

                            for (Token st : name) {
                                sb_precall.append(st.getText());
                            }

                            // Add the table to the current table
                            this.ft.addTable(new Parser(path).getFunctionTable(), sb_precall.toString());
                        } else {
                            token_stack.add(t);
                        }
                        break;
                    case C_INCLUDE_STARTED:
                        break;
                }
            } else {
                // See if a new state needs to be
                if (t.getText().equals("include")) {
                    state = ParserState.INCLUDE_STARTED;
                    continue;
                } else if (t.getText().equals("c_include")) {
                    state = ParserState.C_INCLUDE_STARTED;
                    continue;
                }
            }
        }

        // Make sure the error stack is empty
        if (Parser.error_stack.size() != 0) {
            this._print_error("", 0, "there are errors, flourc exiting");
            System.exit(1);
        }


    }

    /**
     * Constructor used with an import statement (builds the token list into a File)
     *
     * @param imported a list of tokens
     * @throws IOException
     */
    public Parser(List<Token> imported) throws IOException {
        // Build a string of the file location
        StringBuilder sb = new StringBuilder();

        for (Token t : imported) {
            if (t.getText().equals(".")) {
                sb.append(File.separator);
            } else if (t.getText().equals("as")) {

            } else {
                sb.append(t.getText());
            }
        }
        File temp = new File(sb.toString());
        if (!temp.exists()) {
            temp = new File(flourc.LIBFLOUR_LOCATION + sb.toString());
            if (!temp.exists()) {
                this._print_error(sb.toString(), -1, "file not found");
                Parser.error_stack.add("file not found `" + sb.toString() + "`\n");
            }
        }
        new Parser(temp, true);
    }

    public FunctionTable getFunctionTable() {
        return new FunctionTable(this.ft);
    }

    private void _print_error(String filename, int line_num, String error) {
        System.err.println("flourc: parser: " + filename + ":" + line_num + "-> " + error);
        System.err.flush(); // make sure it's printed out NOW
    }

}

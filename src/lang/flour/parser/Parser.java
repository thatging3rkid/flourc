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
        INCLUDE_STARTED, // include keyword found, anything after it is data to be included (one include link per line for now)
        C_INCLUDE_STARTED, // c_include found, send anything after it (until a newline) to the header parser


    }

    public Parser(File file, boolean imported) throws IOException {
        // Start by lexing the file and running it through the preprocessor
        List<Token> token_stream = new Lexer(file).getTokenStream();
        new Preprocessor(token_stream, imported); // til this is a valid java statement

        ParserState state = ParserState.NONE;
        Stack<Token> token_stack = new Stack<>();
        for (Token t : token_stream) {
            // Process the state machine
            if (state != ParserState.NONE) {
                switch (state) {
                    case INCLUDE_STARTED:
                        // Check for the escape character
                        if (t.getText().equals(";")) {
                            Token[] temp = new Token[token_stack.size()];
                            token_stack.toArray(temp);
                            Parser sub = new Parser(Arrays.asList(temp));
                            state = ParserState.NONE;
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

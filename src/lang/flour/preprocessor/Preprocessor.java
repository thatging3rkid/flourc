package lang.flour.preprocessor;

import lang.flour.parser.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Preprocessor {

    private enum PreprocessorState {
        DEFINE_STARTED, DEFINE_NAMED, DEFINE_PARAMS, DEFINE_CODE
    }

    public Preprocessor(List<Token> token_stream) {
        Token cur_tok = null;
        Token old_tok = null;
        PreprocessorState state = null;

        // Go over all the tokens
        for (int i = 0; i < token_stream.size(); i += 1) {
            cur_tok = token_stream.get(i);

            // Check to see if this is a valid preprocessor command
            if (cur_tok.getText().startsWith("@") && (old_tok == null || PreprocessorUtils.isEndOfLine(old_tok))) {
                // Check for the different type of commands
                if (cur_tok.getText().equals("@define")) {
                    state = PreprocessorState.DEFINE_STARTED;
                }
            }

            if (state != null) {
                switch (state) {
                    case DEFINE_STARTED:
                        
                        break;
                    case DEFINE_NAMED:
                        break;
                    case DEFINE_PARAMS:
                        break;
                    case DEFINE_CODE:
                        break;
                }
            }



            // update the old token at the end of the loop
            old_tok = cur_tok;
        }
    }

    private void _print_error(String filename, int line_num, String error) {
        System.err.println("flourc: preprocessor: " + filename + ":" + line_num + "-> " + error);
    }
}

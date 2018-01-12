package lang.flour.preprocessor;

import java.util.Collections;
import java.util.Map;
import lang.flour.lexer.Token;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Preprocessor {

    private enum PreprocessorState {
        DEFINE_STARTED, DEFINE_NAMED, DEFINE_PARAMS, DEFINE_CODE, UNDEF_STARTED
    }

    public Preprocessor(List<Token> token_stream) {
        Token cur_tok = null;
        Token old_tok = null;
        PreprocessorState state = null;

        Map<Integer, Macro> marks = new Hashtable<>();
        Map<String, Macro> macros = new Hashtable<>();
        Macro c_macro = null;

        // Go over all the tokens
        for (int i = 0; i < token_stream.size(); i += 1) {
            cur_tok = token_stream.get(i);

            // Check to see if this is a valid preprocessor command
            if (cur_tok.getText().startsWith("@") && (old_tok == null || PreprocessorUtils.isEndOfLine(old_tok))) {
                // Check for the different type of commands
                if (cur_tok.getText().equals("@define")) {
                    state = PreprocessorState.DEFINE_STARTED;
                    continue;
                } else if (cur_tok.getText().equals("@undef")) {
                    state = PreprocessorState.UNDEF_STARTED;
                    continue;
                } else {
                    this._print_error(cur_tok.getFile_name(), cur_tok.getFile_line(), "unknown preprocessor call + `" + cur_tok.getText() + "`");
                }
            }

            if (state != null) {
                switch (state) {
                    case DEFINE_STARTED:
                        c_macro = new Macro(cur_tok.getText(), null, null);
                        state = PreprocessorState.DEFINE_NAMED;
                        break;
                    case DEFINE_NAMED:
                        // If the token after a name is an open parenthesis, then this
                        if (cur_tok.getText().equals("(")) {
                            state = PreprocessorState.DEFINE_PARAMS;
                        } else {
                            state = PreprocessorState.DEFINE_CODE;
                            c_macro.addReplacementToken(cur_tok);
                        }
                        break;
                    case DEFINE_PARAMS:
                        // check to see if this is an end-of-params character
                        if (cur_tok.getText().equals(")")) {
                            state = PreprocessorState.DEFINE_CODE;
                        } else {
                            // when adding type-checking to macros, this needs to be changed
                            if (cur_tok.getText().equals(",")) {
                                continue;
                            } else {
                                c_macro.addParamToken(cur_tok);
                            }
                        }
                        break;
                    case DEFINE_CODE:
                        // if this is code, check for and end-of-macro (\n), and if not found, then add the token to the replacement
                        if (cur_tok.getText().equals("\n")) {
                            state = null;
                            macros.put(c_macro.getName(), c_macro);
                        } else {
                            c_macro.addReplacementToken(cur_tok);
                        }
                        break;
                    case UNDEF_STARTED:
                        macros.remove(cur_tok.getText());
                        break;
                }
            } else {
                if (macros.get(cur_tok.getText()) != null) {
                    marks.put(i, new Macro(macros.get(cur_tok.getText()))); // note that this is not contextually-aware, similar to C. this could be improved
                }
            }

            // update the old token at the end of the loop
            old_tok = cur_tok;
        }

        // time to complete the insertions, and do the insertions in reverse order so that
        List<Integer> sorted = new ArrayList<>(marks.keySet());
        sorted.sort(Collections.reverseOrder());
        for (int i : sorted) {
            // time to insert the macro
            Macro m = marks.get(i);

            // if no parameters, then just do a raw insert
            if (m.getParams() == null) {
                token_stream.remove(i);
                for (int j = 0; j < m.getReplacement().size(); j += 1) {
                    token_stream.add(i + j, new Token(m.getReplacement().get(j))); // this inserts a copy of the tokens into the stream, so line numbers are preserved (which could be useful for debugging?)
                }
            } else {
                // make a map to bind the parameter name to the replacement name
                Map<String, Token> param_map = new Hashtable<>();
                for (int j = 0; j < m.getParams().size(); j += 1) {
                    param_map.put(m.getParams().get(j).getText(), new Token(token_stream.get(i + (2 * j) + 2)));
                }

                // continue with insertion, but check to see if the param is found, and if so, insert the given parameter
                token_stream.remove(i); // remove the call
                token_stream.remove(i); // remove the parenthesis
                for (int k = 0; k < m.getParams().size() * 2; k += 1) { // remove parameters
                    token_stream.remove(i);
                }

                // replacement
                for (int j = 0; j < m.getReplacement().size(); j += 1) {
                    Token t = new Token(m.getReplacement().get(j));
                    if (param_map.get(t.getText()) != null) {
                        token_stream.add(i + j, new Token(param_map.get(t.getText())));
                    } else {
                        token_stream.add(i + j, t);
                    }
                }
            }
        }
    }

    private void _print_error(String filename, int line_num, String error) {
        System.err.println("flourc: preprocessor: " + filename + ":" + line_num + "-> " + error);
        System.err.flush(); // make sure it's printed out NOW
    }
}

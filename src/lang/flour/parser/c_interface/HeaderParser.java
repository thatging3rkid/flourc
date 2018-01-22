package lang.flour.parser.c_interface;

import java.io.File;
import java.util.List;
import java.util.Stack;
import lang.flour.lexer.Token;
import lang.flour.parser.models.tables.FunctionTable;
import lang.flour.parser.models.tables.TypeTable;

public class HeaderParser {
    private boolean error = false;

    public HeaderParser(List<Token> text, FunctionTable ft, TypeTable tt) {
        // Make sure there are enough tokens
        if (text.size() < 5) {
            System.err.println("flourc: h_parser: not enough tokens, will exit later");
            this.error = true;
            return;
        }

        // Start by getting the file location and name
        boolean local = text.get(0).getText().equals("\"");
        Stack<Token> name = new Stack<>();
        String path = null;

        for (Token t : text.subList(1, text.size())) {
            if (t.getText().equals((local)? "\"" : ">")) {
                StringBuilder sb = new StringBuilder((local)? "" : "/usr/include/");

                for (Token t0 : name) {
                    sb.append(t0.getText());
                }

                path = sb.toString();

                break;
            } else {
                name.add(t);
            }
        }

        // Make sure the operation was competed successfully
        if (path == null) {
            System.err.println("flourc: h_parser: end of path not found, will exit later");
            this.error = true;
            return;
        }

        File fp = new File(path);

        // Make sure the header exists
        if (!fp.exists()) {
            System.err.println("flourc: h_parser: file not found + `" + path + "`, will exit later");
            this.error = true;
            return;
        }

        // Start parsing the file
        List<Token> h_tokens = this._tokenize(fp);
    }

    private List<Token> _tokenize(File fp) {


        return null;
    }

    public boolean hasError() {
        return this.error;
    }

}

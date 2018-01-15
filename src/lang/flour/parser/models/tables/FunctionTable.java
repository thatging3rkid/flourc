package lang.flour.parser.models.tables;

import java.util.ArrayList;
import java.util.List;
import lang.flour.lexer.Token;
import lang.flour.parser.models.codegen.Function;

public class FunctionTable {
    private List<Function> functions;

    public FunctionTable() {
        this.functions = new ArrayList<>();
    }

    public FunctionTable(FunctionTable ft) {
        this.functions = new ArrayList<>(ft.functions);
    }

    public void addFuction(String precall, List<Token> tokens) {
        System.err.println("not implemented");
    }

    public void addTable(FunctionTable ft) {
        System.err.println("not implemented");
    }

    public void addTable(FunctionTable ft, String precall) {
        System.err.println("not implemented");
    }

    public boolean isValid(String precall, String name) {
        for (Function f : this.functions) {
            if (f.getPrecall().equals(precall) && f.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }


}

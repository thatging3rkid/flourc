package lang.flour.parser.models.codegen;

import java.util.List;

public class Function implements Generateable {
    private String precall;
    private String name;
    private List<Identifier> params;
    private List<Expression> code;

    public Function(String precall, String name, List<Identifier> params, List<Expression> code) {
        this.precall = precall;
        this.name = name;
        this.params = params;
        this.code = code;
    }

    public String getPrecall() {
        return precall;
    }

    public String getName() {
        return name;
    }

    @Override
    public String generateIR() {
        return null;
    }
}

package lang.flour.parser.models.codegen;

import java.util.List;

public class Function implements Generateable {
    private String name;
    private List<Identifier> params;
    private List<Expression> code;

    @Override
    public String generateIR() {
        return null;
    }
}

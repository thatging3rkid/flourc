package lang.flour.parser.models.codegen;

import lang.flour.parser.models.Type;

public class Identifier {
    Type type;
    String name;

    public Identifier(Type type, String name) {
        this.type = type;
        this.name = name;
    }


}

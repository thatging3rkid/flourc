package lang.flour.preprocessor;

import java.util.List;

public class Macro {
    private String name;
    private List<String> params;
    private String code;

    public Macro(String name, List<String> params, String code) {
        this.name = name;
        this.params = params;
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public List<String> getParams() {
        return this.params;
    }
}

package lang.flour.parser.models.tables;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import lang.flour.parser.models.Type;

public class SymbolTable {
    private SymbolTable parent;
    private Map<String, Type> entries;

    public SymbolTable() {
        this.parent = null;
        this.entries = new Hashtable<>();
    }

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
        this.entries = new Hashtable<>();
    }

    public Type getType(String name) {
        if (this.entries.containsKey(name)) {
            return this.entries.get(name);
        } else {
            if (parent == null) {
                return null;
            } else {
                return parent.getType(name);
            }
        }
    }

    public void addVariable(String name, Type t) {
        this.entries.put(name, t);
    }

}

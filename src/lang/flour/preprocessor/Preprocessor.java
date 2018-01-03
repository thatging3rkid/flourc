package lang.flour.preprocessor;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Preprocessor {

    private List<String> dependencies = new ArrayList<>();

    public Preprocessor(File file) throws IOException {
        // Process each line (as the preprocessor doesn't use tokenized input)
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder complete_sb = new StringBuilder();
        Hashtable<String, String> pp_symtab = new Hashtable<>();
        int line_num = 0;

        while((line = br.readLine()) != null) {
            complete_sb.append(line);
            line_num += 1;

            // Need to pull in other files
            if (line.startsWith("include") || line.startsWith("c_include")) {
                try {
                    this.dependencies.add(line.split(" ")[1].trim());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("flourc: preprocessor: " + file.getName() + ":" + line_num + ": invalid include statement");
                }
            } else if (line.startsWith("@define")) {
                // preprocessor define statement
                // first, see if this is a macro
                if (line.startsWith("@define(")) {
                    int end_paren_loc = line.indexOf(")");
                    String params = line.substring(8, end_paren_loc).trim();

                }

            }
        }

        // Get the string of the complete file
        String complete = complete_sb.toString();
    }
}

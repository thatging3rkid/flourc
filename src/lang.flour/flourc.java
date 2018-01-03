/**
 * flourc.java
 *
 * Entry point for the flour compiler
 *
 * @author Connor Henley, @thatging3rkid
 */
package lang.flour;

import com.sun.istack.internal.NotNull;
import lang.flour.preprocessor.Preprocessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class flourc {
    public static void main(@NotNull String[] args) {

        // Make sure there are some parameters
        if (args.length == 0) {
            System.err.println("flourc: no arguments given; exiting");
            System.exit(1);
        }

        // Next, start parsing the arguments
        List<String> source_files = new ArrayList<>();

        // For now, just put everything into the preprocessor, no compiler options
        for (String arg : args) {
            //noinspection UseBulkOperation
            source_files.add(arg);
        }

        for (String arg : source_files) {
            File temp = new File(arg);
            // Make sure the file actually exists
            if (!temp.exists()) {
                System.err.println("flourc: file not found `" + arg + "`");
                System.exit(1);
            }

            try {
                Preprocessor fpp = new Preprocessor(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
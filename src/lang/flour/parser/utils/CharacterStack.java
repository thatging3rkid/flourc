/**
 * parser/utils/CharacterStack.java
 *
 * A stack for characters
 *
 * @author Connor Henley, @thatging3rkid
 */
package lang.flour.parser.utils;

import java.util.ArrayList;
import java.util.List;

public class CharacterStack {
    private List<Character> list = new ArrayList<>();

    /**
     * Add something to the top of the stack
     *
     * @param c the character to add
     */
    public void add(char c) {
        list.add(c);
    }

    /**
     * Add something to the stop of the stack
     *
     * @note I'm not sure this is necessary, as I'm not well versed in char auto-boxing, but it's here
     *
     * @param c the character to add
     */
    public void add(Character c) {
        list.add(c);
    }

    /**
     * Empty the stack
     */
    public void clear() {
        this.list = new ArrayList<>();
    }

    /**
     * Clear a certain amount of characters off the top of the stack
     *
     * @param num the number of characters to remove
     */
    public void clear(int num) {
        if (num >= this.list.size()) {
            this.clear();
        } else {
            for (int i = 0; i < num; i += 1) {
                this.list.remove(this.list.size() - 1);
            }
        }
    }

    /**
     * See if the characters on the stack end with a certain string
     *
     * @param str the string to compare against
     * @return the
     */
    public boolean endsWith(String str) {
        return this.toString().endsWith(str);
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public String toString() {
        // Add all the data in the list to a StringBuilder
        StringBuilder sb = new StringBuilder();
        for (Character c : this.list) {
            sb.append(c);
        }
        return sb.toString();
    }

}

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
     * @return true if the stack text ends with the given string, else false
     */
    public boolean endsWith(String str) {
        return this.toString().endsWith(str);
    }

    /**
     * Check if the stack is empty
     *
     * @return true if the stack is empty, else false
     */
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    /**
     * View a character at a position without removing it from the stack
     *
     * @param num the position in the stack to look at (where the top of the stack is 0)
     * @return the character at that position if it exists, else 0
     */
    public Character peek(int num) {
        try {
            return this.list.get(this.list.size() - 1 - num);
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    /**
     * See if all the characters in the stack are whitepsace characters
     *
     * @return true if all the characters in the stack are whitespace characters
     */
    public boolean isAllWhitespace() {
        for (Character c : this.list) {
            if (!LexerUtils.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Remove all whitespace characters from the stack
     */
    public void removeWhitespace() {
        List<Character> temp_list = new ArrayList<>();
        for (int i = 0; i < this.list.size(); i += 1) {
            if (!LexerUtils.isWhitespace(this.list.get(i))) {
                temp_list.add(this.list.get(i));
            }
        }
        this.list = temp_list;
    }

    /**
     * @inherit-doc
     */
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

package lang.flour.parser;

public class Token {
    private String text;
    private String file_name;
    private int file_line;
    private int file_col;

    public Token(String text, String file_name, int file_line, int file_col) {
        this.text = text;
        this.file_name = file_name;
        this.file_line = file_line;
        this.file_col = file_col;
    }

    public String getText() {
        return text;
    }

    public String getFile_name() {
        return file_name;
    }

    public int getFile_line() {
        return file_line;
    }

    public int getFile_col() {
        return file_col;
    }

    @Override
    public String toString() {

        return "`" + this.text + "`:" + this.file_name + ":" + this.file_line + ":" + this.file_col;
    }
}

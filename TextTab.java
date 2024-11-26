import javax.swing.*;
import java.awt.*;

public class TextTab {
    private JTextArea textArea;
    private String fileName;

    public TextTab(String fileName) {
        this(fileName, "");
    }

    public TextTab(String fileName, String content) {
        this.fileName = fileName;
        textArea = new JTextArea(content);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public String getText() {
        return textArea.getText();
    }
}

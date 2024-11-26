import javax.print.DocFlavor.URL;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;
import java.util.*;

public class TextEditor extends JFrame {
    private JTabbedPane tabbedPane;
    private Vector<TextTab> openFiles;
    private JMenuBar menuBar;
    private JTextArea textArea;
    
    public TextEditor() {
        setTitle("Editor de Textos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        openFiles = new Vector<>();
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        textArea = new JTextArea();
        textArea.setEditable(false);

        createMenu();
        createToolbar();
    }
    
    private void createMenu() {
        menuBar = new JMenuBar();
        /*
         * Menú Archivo
         */
        JMenu fileMenu = new JMenu("Archivo");
        JMenuItem newFileItem = new JMenuItem("Nuevo");
        fileMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        newFileItem.addActionListener(e -> createNewFile());
        newFileItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fileMenu.add(newFileItem);
        
        JMenuItem openFileItem = new JMenuItem("Abrir");
        openFileItem.addActionListener(e -> openFile());
        openFileItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fileMenu.add(openFileItem);
        
        JMenuItem saveFileItem = new JMenuItem("Guardar");
        saveFileItem.addActionListener(e -> saveFile());
        fileMenu.add(saveFileItem);
        saveFileItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        JMenuItem exitItem = new JMenuItem("Salir");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        exitItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        menuBar.add(fileMenu);
        /* 
        *Menú Edición
        */
        JMenu editMenu = new JMenu("Edición");
        JMenuItem copyItem = new JMenuItem("Copiar");
        copyItem.addActionListener(e -> copyText());
        copyItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editMenu.add(copyItem);
        editMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        JMenuItem cutItem = new JMenuItem("Cortar");
        cutItem.addActionListener(e -> cutText());
        cutItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editMenu.add(cutItem);
        
        JMenuItem pasteItem = new JMenuItem("Pegar");
        pasteItem.addActionListener(e -> pasteText());
        pasteItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editMenu.add(pasteItem);
        
        JMenuItem selectAllItem = new JMenuItem("Seleccionar todo");
        selectAllItem.addActionListener(e -> selectAllText());
        selectAllItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editMenu.add(selectAllItem);
        
        menuBar.add(editMenu);
        /* 
        *Menú Acerca de Nosotros
        */
        JMenu aboutUs = new JMenu("Acerca de:");
        aboutUs.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        JMenuItem aboutItem = new JMenuItem("Información");
        aboutItem.addActionListener(e -> showAboutInfo()); // Evento para mostrar información
        aboutItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        aboutUs.add(aboutItem);
        
        menuBar.add(aboutUs);

        setJMenuBar(menuBar);
    }
    private void showAboutInfo() {
        java.net.URL imageURL = getClass().getResource("/assets/cut.png");

        if (imageURL != null) {
            String info = "<html><body>"
                + "<img src='" + imageURL + "' style='width:200px;' 'height:100px;' >"
                + "<h1>Sobre Nosotros</h1>"
                + "<h2>Proyecto Integrador</h2>"
                + "<p><b>Materia:</b> POO</p>"
                + "<p><b>Integrantes:</b> Victor, Alexis, Christopher</p>"
                + "</body></html>";
            JEditorPane editorPane = new JEditorPane("text/html", info);
            editorPane.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(editorPane);
            tabbedPane.addTab("Acerca de", scrollPane);
            tabbedPane.setSelectedComponent(scrollPane);
        } else {
            System.out.println("Imagen no encontrada.");
        }
    
    }
    
    private void createToolbar() {
        JToolBar toolbar = new JToolBar();
        
        // Botón de Negrita
        
        JButton boldButton = new JButton(new ImageIcon(getClass().getResource("\\assets\\bold.png")));
        boldButton.addActionListener(e -> toggleBold());
        boldButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boldButton.setToolTipText("Click para activar/desactivar negritas");
        toolbar.add(boldButton);

        
        // Botón de Cursiva
        JButton italicButton = new JButton(new ImageIcon(getClass().getResource("\\assets\\italic.png")));
        italicButton.addActionListener(e -> toggleItalic());
        italicButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        italicButton.setToolTipText("Click para activar/desactivar cursiva");
        toolbar.add(italicButton);
        
        // Aumento de tamaño de la fuente
        JButton increaseFontSizeButton = new JButton(new ImageIcon(getClass().getResource("\\assets\\plus.png")));
        increaseFontSizeButton.addActionListener(e -> changeFontSize(true));
        increaseFontSizeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        increaseFontSizeButton.setToolTipText("Click para aumentar tamaño de letra");
        toolbar.add(increaseFontSizeButton);
        
        // Disminución de tamaño de la fuente
        JButton decreaseFontSizeButton = new JButton(new ImageIcon(getClass().getResource("\\assets\\minus.png")));
        decreaseFontSizeButton.addActionListener(e -> changeFontSize(false));
        decreaseFontSizeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        decreaseFontSizeButton.setToolTipText("Click para disminuir tamaño de letra");
        toolbar.add(decreaseFontSizeButton);
        
        add(toolbar, BorderLayout.NORTH);
    }
    
    private void createNewFile() {
        TextTab newTab = new TextTab("Nuevo Archivo");
        openFiles.add(newTab);
        tabbedPane.addTab("Nuevo Archivo", newTab.getTextArea());
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1); 
    }
    
    
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();
                TextTab newTab = new TextTab(file.getName(), content.toString());
                openFiles.add(newTab);
                tabbedPane.addTab(file.getName(), newTab.getTextArea());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void saveFile() {
        int index = tabbedPane.getSelectedIndex();
        if (index != -1) {
            TextTab currentTab = openFiles.get(index);
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    writer.write(currentTab.getText());
                    writer.close();
                    tabbedPane.setTitleAt(index, file.getName());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al guardar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void copyText() {
        int index = tabbedPane.getSelectedIndex();
        if (index != -1) {
            TextTab currentTab = openFiles.get(index);
            currentTab.getTextArea().copy();
        }
    }
    
    private void cutText() {
        int index = tabbedPane.getSelectedIndex();
        if (index != -1) {
            TextTab currentTab = openFiles.get(index);
            currentTab.getTextArea().cut();
        }
    }
    
    private void pasteText() {
        int index = tabbedPane.getSelectedIndex();
        if (index != -1) {
            TextTab currentTab = openFiles.get(index);
            currentTab.getTextArea().paste();
        }
    }
    
    private void selectAllText() {
        int index = tabbedPane.getSelectedIndex();
        if (index != -1) {
            TextTab currentTab = openFiles.get(index);
            currentTab.getTextArea().selectAll();
        }
    }
    
    private void toggleBold() {
        int index = tabbedPane.getSelectedIndex();
        if (index != -1) {
            TextTab currentTab = openFiles.get(index);
            Font currentFont = currentTab.getTextArea().getFont();
            if (currentFont.isBold()) {
                currentTab.getTextArea().setFont(currentFont.deriveFont(Font.PLAIN));
            } else {
                currentTab.getTextArea().setFont(currentFont.deriveFont(Font.BOLD));
            }
        }
    }
    
    private void toggleItalic() {
        int index = tabbedPane.getSelectedIndex();
        if (index != -1) {
            TextTab currentTab = openFiles.get(index);
            Font currentFont = currentTab.getTextArea().getFont();
            if (currentFont.isItalic()) {
                currentTab.getTextArea().setFont(currentFont.deriveFont(Font.PLAIN));
            } else {
                currentTab.getTextArea().setFont(currentFont.deriveFont(Font.ITALIC));
            }
        }
    }
    
    private void changeFontSize(boolean increase) {
        int index = tabbedPane.getSelectedIndex();
        if (index != -1) {
            TextTab currentTab = openFiles.get(index);
            Font currentFont = currentTab.getTextArea().getFont();
            int newSize = (int) currentFont.getSize();
            if (increase) {
                newSize += 2;
            } else {
                newSize -= 2;
            }
            currentTab.getTextArea().setFont(new Font(currentFont.getName(), currentFont.getStyle(), newSize));
        }
    }
}

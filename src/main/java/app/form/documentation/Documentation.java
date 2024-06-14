package app.form.documentation;

import app.model.User;
import app.notifications.Notifications;
import app.service.DBService;
import app.toolbox.BackIcon;
import app.toolbox.MenuInfo;
import app.toolbox.UserInfo;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static app.toolbox.StyleLoginUser.ChekStyle;

public class Documentation extends JPanel implements ActionListener {
    // Variables declaration - do not modify
    @Autowired
    private DBService dbService;
    private JFrame frame;
    private MenuInfo menuInfo;
    private HTMLDocument document;
    private JTextPane textPane = new JTextPane();
    private boolean debug = false;
    private File currentFile;
    private UserInfo userInfo;

    /** Listener for the edits on the current document. */
    protected UndoableEditListener undoHandler = new Documentation.UndoHandler();

    /** UndoManager that we add edits to. */
    protected UndoManager undo = new UndoManager();
    private Documentation.UndoAction undoAction = new Documentation.UndoAction();
    private Documentation.RedoAction redoAction = new Documentation.RedoAction();
    private Action cutAction = new DefaultEditorKit.CutAction();
    private Action copyAction = new DefaultEditorKit.CopyAction();
    private Action pasteAction = new DefaultEditorKit.PasteAction();
    private Action boldAction = new StyledEditorKit.BoldAction();
    private Action underlineAction = new StyledEditorKit.UnderlineAction();
    private Action italicAction = new StyledEditorKit.ItalicAction();
    private Action insertBreakAction = new DefaultEditorKit.InsertBreakAction();
    // End of variables declaration

    @Autowired
    public Documentation(JFrame frame, DBService dbService) {
        HTMLEditorKit editorKit = new HTMLEditorKit();
        document = (HTMLDocument)editorKit.createDefaultDocument();
        // Force SwingSet to come up in the Cross Platform L&F
        try {
            // If you want the System L&F instead, comment out the above line and
            // uncomment the following:
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exc) {
            System.err.println("Error loading L&F: " + exc);
        }
        this.dbService = dbService;
        this.frame = frame;
        this.menuInfo = new MenuInfo();
        init();
    }

    private void init(){
        //check stayle
        userInfo = new UserInfo(dbService);
        String output = userInfo.getUserName();
        User berechtigung = dbService.findByUsername(output);
        ChekStyle(berechtigung);

        menuInfo.createMenu(frame, dbService); // Assuming 'frame' is your JFrame instance


        //add save button
        JLabel labIconSavMemo = new JLabel();
        FlatSVGIcon iconMemo = new FlatSVGIcon("svg/save.svg");
        labIconSavMemo.setIcon(new FlatSVGIcon(iconMemo));

        labIconSavMemo.addMouseListener(new MouseAdapter() {


            @Override
            public void mouseClicked(MouseEvent e) {

                try {
                    String bezPrvog = textPane.getText().substring(45);
                    String bezZadnjeg = bezPrvog.substring(0, bezPrvog.length() - 20);
                    DateTimeFormatter datumFormater = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    LocalDate datum = LocalDate.now(); // Dobivanje trenutnog datuma
                    String formatiraniDatum = datum.format(datumFormater); // Formatiranje trenutnog datuma prema zadanim obrascu
                    dbService.saveAllDocu(bezZadnjeg+"<p style=\"margin-top: 0\">\n" +
                            "      ----------------- Datum: " + formatiraniDatum + " --------------------" +
                            "    </p>");

                }catch (Exception ee){
                    dbService.LogToDatabase("ERROR", "Invalid Docu save, Documentation.java"+ee,output); // log if not working
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, "Dokumentation update upadte");
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labIconSavMemo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labIconSavMemo.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

        });


        setLayout(new MigLayout("", "[grow]", "")); // dva stupca za raspored panela
        setBackground(Color.YELLOW);


// Menu Panel -> Panel left
        JPanel menuPanelLeft = new JPanel(new MigLayout("", "", ""));

        JLabel labMenu = BackIcon.getBackIconDaschboad(frame, dbService);

        //menuPanelLeft.setBackground(Color.GREEN);
        menuPanelLeft.add(labMenu, "span, split");
        menuPanelLeft.add(labIconSavMemo);

        // Dodavanje originalnih menu panela
        add(menuPanelLeft, "dock north, h 50px");

        JMenuBar menuBar = new JMenuBar();
        add(menuBar, BorderLayout.NORTH);

        JMenu editMenu = new JMenu("Edit");
        JMenu colorMenu = new JMenu("Color");
        JMenu fontMenu = new JMenu("Font");
        JMenu styleMenu = new JMenu("Style");
        JMenu alignMenu = new JMenu("Align");


        menuBar.add(editMenu);
        menuBar.add(colorMenu);
        menuBar.add(fontMenu);
        menuBar.add(styleMenu);
        menuBar.add(alignMenu);

        JMenuItem newItem = new JMenuItem("New", new ImageIcon("whatsnew-bang.gif"));
        JMenuItem openItem = new JMenuItem("Open",new ImageIcon("open.gif"));
        JMenuItem saveItem = new JMenuItem("Save",new ImageIcon("save.gif"));
        JMenuItem saveAsItem = new JMenuItem("Save As");
        JMenuItem exitItem = new JMenuItem("Exit",new ImageIcon("exit.gif"));

        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        saveAsItem.addActionListener(this);
        exitItem.addActionListener(this);

        JMenuItem undoItem = new JMenuItem(undoAction);
        JMenuItem redoItem = new JMenuItem(redoAction);
        JMenuItem cutItem = new JMenuItem(cutAction);
        JMenuItem copyItem = new JMenuItem(copyAction);
        JMenuItem pasteItem = new JMenuItem(pasteAction);
        JMenuItem clearItem = new JMenuItem("Clear");
        JMenuItem selectAllItem = new JMenuItem("Select All");
        JMenuItem insertBreaKItem = new JMenuItem(insertBreakAction);

        cutItem.setText("Cut");
        copyItem.setText("Copy");
        pasteItem.setText("Paste");
        insertBreaKItem.setText("Break");
        cutItem.setIcon(new ImageIcon("cut.gif"));
        copyItem.setIcon(new ImageIcon("copy.gif"));
        pasteItem.setIcon(new ImageIcon("paste.gif"));
        insertBreaKItem.setIcon(new ImageIcon("break.gif"));

        clearItem.addActionListener(this);
        selectAllItem.addActionListener(this);

        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.add(clearItem);
        editMenu.add(selectAllItem);
        editMenu.add(insertBreaKItem);

        JMenuItem redTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Red",Color.red));
        JMenuItem orangeTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Orange",Color.orange));
        JMenuItem yellowTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Yellow",Color.yellow));
        JMenuItem greenTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Green",Color.green));
        JMenuItem blueTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Blue",Color.blue));
        JMenuItem cyanTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Cyan",Color.cyan));
        JMenuItem magentaTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Magenta",Color.magenta));
        JMenuItem blackTextItem = new JMenuItem(new StyledEditorKit.ForegroundAction("Black",Color.black));


        redTextItem.setIcon(new ImageIcon("red.gif"));
        orangeTextItem.setIcon(new ImageIcon("orange.gif"));
        yellowTextItem.setIcon(new ImageIcon("yellow.gif"));
        greenTextItem.setIcon(new ImageIcon("green.gif"));
        blueTextItem.setIcon(new ImageIcon("blue.gif"));
        cyanTextItem.setIcon(new ImageIcon("cyan.gif"));
        magentaTextItem.setIcon(new ImageIcon("magenta.gif"));
        blackTextItem.setIcon(new ImageIcon("black.gif"));

        colorMenu.add(redTextItem);
        colorMenu.add(orangeTextItem);
        colorMenu.add(yellowTextItem);
        colorMenu.add(greenTextItem);
        colorMenu.add(blueTextItem);
        colorMenu.add(cyanTextItem);
        colorMenu.add(magentaTextItem);
        colorMenu.add(blackTextItem);

        JMenu fontTypeMenu = new JMenu("Font Type");
        fontMenu.add(fontTypeMenu);

        String[] fontTypes = {"SansSerif", "Serif", "Monospaced", "Dialog", "DialogInput"};
        for (int i = 0; i < fontTypes.length;i++){
            if (debug) System.out.println(fontTypes[i]);
            JMenuItem nextTypeItem = new JMenuItem(fontTypes[i]);
            nextTypeItem.setAction(new StyledEditorKit.FontFamilyAction(fontTypes[i], fontTypes[i]));
            fontTypeMenu.add(nextTypeItem);
        }

        JMenu fontSizeMenu = new JMenu("Font Size");
        fontMenu.add(fontSizeMenu);

        int[] fontSizes = {6, 8,10,12,14, 16, 20,24, 32,36,48,72};
        for (int i = 0; i < fontSizes.length;i++){
            if (debug) System.out.println(fontSizes[i]);
            JMenuItem nextSizeItem = new JMenuItem(String.valueOf(fontSizes[i]));
            nextSizeItem.setAction(new StyledEditorKit.FontSizeAction(String.valueOf(fontSizes[i]), fontSizes[i]));
            fontSizeMenu.add(nextSizeItem);



        }


        JMenuItem boldMenuItem = new JMenuItem(boldAction);
        JMenuItem underlineMenuItem = new JMenuItem(underlineAction);
        JMenuItem italicMenuItem = new JMenuItem(italicAction);

        boldMenuItem.setText("Bold");
        underlineMenuItem.setText("Underline");
        italicMenuItem.setText("Italic");

        boldMenuItem.setIcon(new ImageIcon("bold.gif"));
        underlineMenuItem.setIcon(new ImageIcon("underline.gif"));
        italicMenuItem.setIcon(new ImageIcon("italic.gif"));

        styleMenu.add(boldMenuItem);
        styleMenu.add(underlineMenuItem);
        styleMenu.add(italicMenuItem);

        JMenuItem subscriptMenuItem = new JMenuItem(new Documentation.SubscriptAction());
        JMenuItem superscriptMenuItem = new JMenuItem(new Documentation.SuperscriptAction());
        JMenuItem strikeThroughMenuItem = new JMenuItem(new Documentation.StrikeThroughAction());

        subscriptMenuItem.setText("Subscript");
        superscriptMenuItem.setText("Superscript");
        strikeThroughMenuItem.setText("StrikeThrough");

        subscriptMenuItem.setIcon(new ImageIcon("subscript.gif"));
        superscriptMenuItem.setIcon(new ImageIcon("superscript.gif"));
        strikeThroughMenuItem.setIcon(new ImageIcon("strikethough.gif"));

        styleMenu.add(subscriptMenuItem);
        styleMenu.add(superscriptMenuItem);
        styleMenu.add(strikeThroughMenuItem);

        JMenuItem leftAlignMenuItem = new JMenuItem(new StyledEditorKit.AlignmentAction("Left Align",StyleConstants.ALIGN_LEFT));
        JMenuItem centerMenuItem = new JMenuItem(new StyledEditorKit.AlignmentAction("Center",StyleConstants.ALIGN_CENTER));
        JMenuItem rightAlignMenuItem = new JMenuItem(new StyledEditorKit.AlignmentAction ("Right Align",StyleConstants.ALIGN_RIGHT));

        leftAlignMenuItem.setText("Left Align");
        centerMenuItem.setText("Center");
        rightAlignMenuItem.setText("Right Align");

        leftAlignMenuItem.setIcon(new ImageIcon("left.gif"));
        centerMenuItem.setIcon(new ImageIcon("center.gif"));
        rightAlignMenuItem.setIcon(new ImageIcon("right.gif"));

        alignMenu.add(leftAlignMenuItem);
        alignMenu.add(centerMenuItem);
        alignMenu.add(rightAlignMenuItem);


        JPanel editorControlPanel = new JPanel();
        editorControlPanel.setLayout(new FlowLayout());

        /* JButtons */
        JButton cutButton = new JButton(cutAction);
        JButton copyButton = new JButton(copyAction);
        JButton pasteButton = new JButton(pasteAction);

        JButton boldButton = new JButton(boldAction);
        JButton underlineButton = new JButton(underlineAction);
        JButton italicButton = new JButton(italicAction);

        cutButton.setText("Cut");
        copyButton.setText("Copy");
        pasteButton.setText("Paste");

        boldButton.setText("Bold");
        underlineButton.setText("Underline");
        italicButton.setText("Italic");

        cutButton.setIcon(new ImageIcon("cut.gif"));
        copyButton.setIcon(new ImageIcon("copy.gif"));
        pasteButton.setIcon(new ImageIcon("paste.gif"));

        boldButton.setIcon(new ImageIcon("bold.gif"));
        underlineButton.setIcon(new ImageIcon("underline.gif"));
        italicButton.setIcon(new ImageIcon("italic.gif"));

        editorControlPanel.add(cutButton);
        editorControlPanel.add(copyButton);
        editorControlPanel.add(pasteButton);

        editorControlPanel.add(boldButton);
        editorControlPanel.add(underlineButton);
        editorControlPanel.add(italicButton);

        JButton subscriptButton = new JButton(new Documentation.SubscriptAction());
        JButton superscriptButton = new JButton(new Documentation.SuperscriptAction());
        JButton strikeThroughButton = new JButton(new Documentation.StrikeThroughAction());

        subscriptButton.setIcon(new ImageIcon("subscript.gif"));
        superscriptButton.setIcon(new ImageIcon("superscript.gif"));
        strikeThroughButton.setIcon(new ImageIcon("strikethough.gif"));


        JPanel specialPanel = new JPanel();
        specialPanel.setLayout(new FlowLayout());

        specialPanel.add(subscriptButton);
        specialPanel.add(superscriptButton);
        specialPanel.add(strikeThroughButton);

        JButton leftAlignButton = new JButton(new StyledEditorKit.AlignmentAction("Left Align",StyleConstants.ALIGN_LEFT));
        JButton centerButton = new JButton(new StyledEditorKit.AlignmentAction("Center",StyleConstants.ALIGN_CENTER));
        JButton rightAlignButton = new JButton(new StyledEditorKit.AlignmentAction ("Right Align",StyleConstants.ALIGN_RIGHT));
        JButton colorButton = new JButton(new StyledEditorKit.AlignmentAction ("Right Align",StyleConstants.ALIGN_RIGHT));

        leftAlignButton.setIcon(new ImageIcon("left.gif"));
        centerButton.setIcon(new ImageIcon("center.gif"));
        rightAlignButton.setIcon(new ImageIcon("right.gif"));
        colorButton.setIcon(new ImageIcon("color.gif"));

        leftAlignButton.setText("Left Align");
        centerButton.setText("Center");
        rightAlignButton.setText("Right Align");

        JPanel alignPanel = new JPanel();
        alignPanel.setLayout(new FlowLayout());
        alignPanel.add(leftAlignButton);
        alignPanel.add(centerButton);
        alignPanel.add(rightAlignButton);

        document.addUndoableEditListener(undoHandler);
        resetUndoManager();

        textPane = new JTextPane();

        textPane.setContentType("text/html");
        //insert from docu
        String doc = dbService.selectAllDocu();
        String docuSub = doc.substring(1, doc.length() - 1);
        textPane.setText(docuSub);


        JScrollPane scrollPane = new JScrollPane(textPane);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Dimension scrollPaneSize = new Dimension(5*screenSize.width/8,5*screenSize.height/8);
        Dimension scrollPaneSize = new Dimension(screenSize.width, screenSize.height);

        scrollPane.setPreferredSize(scrollPaneSize);

        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new BorderLayout());
        toolPanel.add(editorControlPanel, BorderLayout.NORTH);
        toolPanel.add(specialPanel, BorderLayout.CENTER);
        toolPanel.add(alignPanel, BorderLayout.SOUTH);
        add(menuBar, BorderLayout.NORTH);
        //getContentPane().add(toolPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        show();
    }

    public void actionPerformed(ActionEvent ae){
        String actionCommand = ae.getActionCommand();
        if (debug){
            int modifier = ae.getModifiers();
            long when = ae.getWhen();
            String parameter = ae.paramString();
            System.out.println("actionCommand: " + actionCommand);
            System.out.println("modifier: " + modifier);
            System.out.println("when: " + when);
            System.out.println("parameter: " + parameter);
        }
        if (actionCommand.compareTo("Select All") == 0){
            selectAll();
        }
    }

    protected void resetUndoManager() {
        undo.discardAllEdits();
        undoAction.update();
        redoAction.update();
    }

    public void selectAll(){
        textPane.selectAll();
    }


    class SubscriptAction extends StyledEditorKit.StyledTextAction{

        public SubscriptAction(){
            super(StyleConstants.Subscript.toString());
        }
        public void actionPerformed(ActionEvent ae){
            JEditorPane editor = getEditor(ae);
            if (editor != null) {
                StyledEditorKit kit = getStyledEditorKit(editor);
                MutableAttributeSet attr = kit.getInputAttributes();
                boolean subscript = (StyleConstants.isSubscript(attr)) ? false : true;
                SimpleAttributeSet sas = new SimpleAttributeSet();
                StyleConstants.setSubscript(sas, subscript);
                setCharacterAttributes(editor, sas, false);
            }
        }
    }

    class SuperscriptAction extends StyledEditorKit.StyledTextAction{

        public SuperscriptAction(){
            super(StyleConstants.Superscript.toString());
        }
        public void actionPerformed(ActionEvent ae){
            JEditorPane editor = getEditor(ae);
            if (editor != null) {
                StyledEditorKit kit = getStyledEditorKit(editor);
                MutableAttributeSet attr = kit.getInputAttributes();
                boolean superscript = (StyleConstants.isSuperscript(attr)) ? false : true;
                SimpleAttributeSet sas = new SimpleAttributeSet();
                StyleConstants.setSuperscript(sas, superscript);
                setCharacterAttributes(editor, sas, false);
            }
        }
    }

    class StrikeThroughAction extends StyledEditorKit.StyledTextAction{

        public StrikeThroughAction(){
            super(StyleConstants.StrikeThrough.toString());
        }

        public void actionPerformed(ActionEvent ae){
            JEditorPane editor = getEditor(ae);
            if (editor != null) {
                StyledEditorKit kit = getStyledEditorKit(editor);
                MutableAttributeSet attr = kit.getInputAttributes();
                boolean strikeThrough = (StyleConstants.isStrikeThrough(attr)) ? false : true;
                SimpleAttributeSet sas = new SimpleAttributeSet();
                StyleConstants.setStrikeThrough(sas, strikeThrough);
                setCharacterAttributes(editor, sas, false);
            }
        }
    }


    class HTMLFileFilter extends javax.swing.filechooser.FileFilter{

        public boolean accept(File f){
            return ((f.isDirectory()) ||(f.getName().toLowerCase().indexOf(".htm") > 0));
        }

        public String getDescription(){
            return "html";
        }
    }

    class UndoHandler implements UndoableEditListener {

        /**
         * Messaged when the Document has created an edit, the edit is
         * added to <code>undo</code>, an instance of UndoManager.
         */
        public void undoableEditHappened(UndoableEditEvent e) {
            undo.addEdit(e.getEdit());
            undoAction.update();
            redoAction.update();
        }
    }

    class UndoAction extends AbstractAction {
        public UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            update();
            redoAction.update();
        }

        protected void update() {
            if(undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getUndoPresentationName());
            }else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }

    class RedoAction extends AbstractAction {

        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
                System.err.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            update();
            undoAction.update();
        }

        protected void update() {
            if(undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getRedoPresentationName());
            }else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }


    // Metoda za dobivanje HTML sadržaja iz textPane
    private String getHTMLFromTextPane() {
        try {
            // Kreiranje HTML dokumenta iz textPane
            HTMLDocument doc = (HTMLDocument) textPane.getDocument();
            StringWriter writer = new StringWriter();
            //doc.getStyleSheet().write(writer);
            String style = writer.toString();

            // Konverzija textPane sadržaja u HTML format
            StringWriter out = new StringWriter();
            new HTMLEditorKit().write(out, doc, doc.getStartPosition().getOffset(), doc.getLength());

            // Vraćanje HTML sadržaja
            return "<html><head>" + style + "</head><body>" + out.toString() + "</body></html>";
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

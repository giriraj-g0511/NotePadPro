package functionality;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.Serializable;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public final class NoteIt {
	OnClick oc = new OnClick();
	
	JFrame frame;
	JTextArea textArea;
	JMenuBar menuBar;
	JMenu menuFile,menuEdit,menuFormat,menuProEditor,menuCMD;
	
	String data="",fontName="San Serif";
	int fontSize = 12;
	int textAreaLength=0;
	String path = null ,fileName = null;
	boolean wrap = false;
	
	
	/*	File items			*/
	JMenuItem itemNew,itemNewWindow,itemOpen,itemSave,itemSaveAs,itemExit;
	/*	Edit items			*/
	JMenuItem itemUndo,itemRedo,itemClear,itemDateTime;
	/*	Format items		*/
	JMenuItem itemWorWrap,itemFont,itemFontSize;
	/*	ProEditor items		*/
	JMenuItem itemJava,itemPython,itemC,itemHTML,itemCpp,itemOpenCMD;
	
	
	public NoteIt() {
		createFrame();
		createTextArea();
		createMenuBar();
		createScrollBar();
		addFileItmes();
		addEditItems();
		addFormatItems();
		createProEditorItems();
	}

	private void createFrame() {
		frame = new JFrame("Untitled");
		frame.setSize(1000,500);
		Image icon = Toolkit.getDefaultToolkit().getImage("dataNoteIt\\img\\NoteIt.png").getScaledInstance(10000, 10000, Image.SCALE_REPLICATE);
		frame.setIconImage(icon);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(oc.close(this));
		
		}
	
	private void createTextArea() {
		textArea = new JTextArea();
		frame.add(textArea);
}
	
	private void createMenuBar() {
		menuBar = new JMenuBar();
		menuFile = new JMenu("File");
		menuEdit = new JMenu("Edit");
		menuFormat = new JMenu("Format");
		menuProEditor = new JMenu("ProEditor");
		menuCMD = new JMenu("Open Command Prompt");
		
		menuBar.add(menuFile);
		menuBar.add(menuEdit);
		menuBar.add(menuFormat);
		menuBar.add(menuProEditor);
		menuBar.add(menuCMD);
		
		itemOpenCMD = new JMenuItem("Open CMD");
		menuCMD.add(itemOpenCMD);
		itemOpenCMD.addActionListener(oc.openCMD(this));
		
		
		frame.setJMenuBar(menuBar);
		
	}
	
	
	
	private void createScrollBar() {
		JScrollPane scrollBar = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.add(scrollBar);
		
	}
	
	
	private void addFileItmes() {
		
		itemNew = new JMenuItem("New");
		itemNewWindow = new JMenuItem("New Window");
		itemOpen = new JMenuItem("Open");
		itemSave = new JMenuItem("Save");
		itemSaveAs = new JMenuItem("Sava As");
		itemExit = new JMenuItem("Exit");

		/*	New Action		*/
		itemNew.addActionListener(oc.newOptn(this));
		
		/*	New Window Action	*/
		itemNewWindow.addActionListener(oc.newWindow());
		
		/*	Save Action		*/
		itemSave.addActionListener(oc.save(this));
		
		/*	Save AS Action	*/
		itemSaveAs.addActionListener(oc.saveAs(this));
		
		/*	Open Action		*/
		itemOpen.addActionListener(oc.open(this));
		
		/*	Exist Window	*/
		itemExit.addActionListener(oc.exit(this));
		
		
		
		
		menuFile.add(itemNew);
		menuFile.add(itemNewWindow);
		menuFile.add(itemOpen);
		menuFile.add(itemSave);
		menuFile.add(itemSaveAs);
		menuFile.add(itemExit);
	}
	
	private void addEditItems() {
		itemUndo = new JMenuItem("Undo : NW");
		itemRedo = new JMenuItem("Redo : NW");
		itemClear = new JMenuItem("Clear");
		itemDateTime = new JMenuItem("Date/Time");
		
		itemClear.addActionListener(oc.clearScreen(this));
		itemDateTime.addActionListener(oc.displayDateAndTime(this));
		
		menuEdit.add(itemUndo);
		menuEdit.add(itemRedo);
		menuEdit.add(itemClear);
		menuEdit.add(itemDateTime);
		
	}
	
	private void addFormatItems() {
		

		/*		Word Wrap Implementation		*/
		itemWorWrap = new JMenuItem("Word Wrap : Off");
		itemWorWrap.addActionListener(oc.wordWrap(this));	


		/*		Font Implementation		*/
		itemFont = new JMenu("Font : San Serif");
		JMenuItem fontArial = new JMenuItem("Arial");
		JMenuItem fontConsolas = new JMenuItem("Consolas");
		JMenuItem fontMonospaced = new JMenuItem("Monospaced");
		JMenuItem fontSanSerif = new JMenuItem("San Serif");
		JMenuItem fontSerif = new JMenuItem("Serif");
		JMenuItem fontTimesNewRoman = new JMenuItem("Times New Roman");
		
		fontArial.addActionListener(oc.setFont("Arial", this));
		fontConsolas.addActionListener(oc.setFont("Consolas", this));
		fontMonospaced.addActionListener(oc.setFont("Monospaced", this));
		fontSanSerif.addActionListener(oc.setFont("SanSerif", this));
		fontSerif.addActionListener(oc.setFont("Serif", this));
		fontTimesNewRoman.addActionListener(oc.setFont("Times New Roman", this));

		itemFont.add(fontArial);
		itemFont.add(fontConsolas);
		itemFont.add(fontMonospaced);
		itemFont.add(fontSanSerif);
		itemFont.add(fontSerif);
		itemFont.add(fontTimesNewRoman);
		
		
		/*		Font Size Implementation		*/
		itemFontSize = new JMenu("Font size : 12");
		JMenuItem fontSize4 = new JMenuItem("4");
		JMenuItem fontSize8 = new JMenuItem("8");
		JMenuItem fontSize12 = new JMenuItem("12");
		JMenuItem fontSize16 = new JMenuItem("16");
		JMenuItem fontSize20 = new JMenuItem("20");
		JMenuItem fontSize24 = new JMenuItem("24");
		JMenuItem fontSize28 = new JMenuItem("28");
		JMenuItem fontSize45 = new JMenuItem("45");

		fontSize4.addActionListener(oc.setSize(4,this));
		fontSize8.addActionListener(oc.setSize(8,this));
		fontSize12.addActionListener(oc.setSize(12,this));
		fontSize16.addActionListener(oc.setSize(16,this));
		fontSize20.addActionListener(oc.setSize(20,this));
		fontSize24.addActionListener(oc.setSize(24,this));
		fontSize28.addActionListener(oc.setSize(28,this));
		fontSize45.addActionListener(oc.setSize(45,this));
		
		
		itemFontSize.add(fontSize4);
		itemFontSize.add(fontSize8);
		itemFontSize.add(fontSize12);
		itemFontSize.add(fontSize16);
		itemFontSize.add(fontSize20);
		itemFontSize.add(fontSize24);
		itemFontSize.add(fontSize28);
		itemFontSize.add(fontSize45);
		
		
		/*	Adding Format menu to frame		*/
		menuFormat.add(itemWorWrap);
		menuFormat.add(itemFont);
		menuFormat.add(itemFontSize);
		
	}
	
	private void createProEditorItems() {
		
	
		
		itemC = new JMenuItem("C");
		itemCpp = new JMenuItem("C++");
		itemHTML = new JMenuItem("HTML");
		itemJava = new JMenuItem("Java");
		itemPython = new JMenuItem("Python");
		
		String codePath = "dataNoteIt\\";
		
		itemC.addActionListener(oc.openProEditor(codePath+"c.txt"));
		itemCpp.addActionListener(oc.openProEditor(codePath+"cpp.txt"));
		itemHTML.addActionListener(oc.openProEditor(codePath+"html.txt"));
		itemJava.addActionListener(oc.openProEditor(codePath+"java.txt"));
		itemPython.addActionListener(oc.openProEditor(codePath+"python.txt"));
		
		
		menuProEditor.add(itemC);
		menuProEditor.add(itemCpp);
		menuProEditor.add(itemHTML);
		menuProEditor.add(itemJava);
		menuProEditor.add(itemPython);
		
	}

}

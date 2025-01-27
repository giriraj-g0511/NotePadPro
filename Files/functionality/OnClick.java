package functionality;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JOptionPane;

final class OnClick {

	LinkedHashMap<String, MetaData> metadata = new LinkedHashMap<String, MetaData>();
	String mdFilePath = "dataNoteIt\\data\\metadata.txt";

	public OnClick() {
	}

	/*
	 * it work like flag for new option to identify which method calling clearscreen
	 */
	/* clear and new both call clear screen */
	int flag = -1;
	/*
	 * if user click on save but dont save the file for that this variable this will
	 * be true after writing data
	 */

	public ActionListener newOptn(NoteIt nt) {
		return (ActionEvent e) -> {
			/* Ask for save, dont save or cancel */
			flag = 1;

			if (!isChangesDone(nt)) {
				isNeedToSave(nt).actionPerformed(e);
			} else {
				nt.textArea.setText("");
				nt.frame.setTitle("Untitled");
				;
			}
		};
	}

	private boolean isChangesDone(NoteIt nt) {
		boolean isDataAltered, isDataUpdated;
		String data;
		try {
			data = nt.textArea.getText();

		} catch (NullPointerException npe) {
			/* because it throws npe when textarea is null */
			data = "";
		}

		isDataAltered = (nt.data.equals(data));

		isDataUpdated = nt.textAreaLength == nt.textArea.getText().length();

		return isDataUpdated || isDataAltered;
	}

	public ActionListener newWindow() {

		return (ActionEvent e) -> new NoteIt();
	}

	public ActionListener open(NoteIt nt) {
		return (ActionEvent e) -> {
			FileDialog fd = new FileDialog(nt.frame, "Open", FileDialog.LOAD);
			fd.setVisible(true);

			/* To Check file is present or not */
			Path testPath = Paths.get(fd.getDirectory() + fd.getFile());
			boolean isValidFile = testPath.toFile().isFile();

			/* If current textarea is not empty then open file in new window */
			if (nt.textArea.getText().isEmpty()) {
				if (isValidFile) {
					nt.path = fd.getDirectory();
					nt.fileName = fd.getFile();
					bufferReader(nt.path + nt.fileName, nt);
				} else if (fd.getFile() != null) {
					/* if user enter some random characters and try to open then show */
					showErrorMessage(nt,
							"The file you are trying to open could not be found. Please check the file name or path and try again.",
							"File is missing");
				}

			} else {
				if (isValidFile) {
					NoteIt noteIt = new NoteIt();
					noteIt.path = fd.getDirectory();
					noteIt.fileName = fd.getFile();
					bufferReader(nt.path + nt.fileName, noteIt);
				} else if (fd.getFile() != null) {
					/* if user enter some random characters and try to open then show */
					showErrorMessage(nt,
							"The file you are trying to open could not be found. Please check the file name or path and try again.",
							"File is missing");
				}
			}
		};
	}

	public ActionListener save(NoteIt nt) {
		return (ActionEvent e) -> {
			if (nt.fileName == null) {
				saveAs(nt).actionPerformed(e);
			} else if (Path.of(nt.path + nt.fileName).toFile().isFile()) {
				bufferWriter(nt);
			} else {
				showErrorMessage(nt, "Wrong File name or File Path", "Error");
			}
		};
	}

	public ActionListener saveAs(NoteIt nt) {
		return (ActionEvent e) -> {
			FileDialog fd = new FileDialog(nt.frame, "Save As", FileDialog.SAVE);
			/* Initialy the file name will be .txt* */
			fd.setFile(".txt*");
			fd.setVisible(true);

			if (fd.getDirectory() != null && fd.getFile() != null) {
				Path path = Paths.get(fd.getDirectory());
				boolean isValidPath = path.toFile().canExecute();
				if (isValidPath) {
					nt.path = fd.getDirectory();
					nt.fileName = fd.getFile();
					nt.frame.setTitle(fd.getFile());
					bufferWriter(nt);
				} else {
					showErrorMessage(nt, "Wrong File name or File Path", "Error");
				}
			}

		};
	}

	private void bufferWriter(NoteIt nt) {
		String path = nt.path + nt.fileName;
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {

			// to write the font name and font size
			readMetaData(nt);
			writeMetaData(path, nt);
			String dataToWrite = nt.textArea.getText();
			/* line seperator is used to maintain line spacing */
			bw.write(dataToWrite + System.lineSeparator());
			// Saving the changes after writing data
			nt.data = dataToWrite;
			nt.textAreaLength = dataToWrite.length();
			/* Updating frame title */
			nt.frame.setTitle(nt.fileName);
		} catch (IOException ioe) {
			/* IOE Dialog box */

			/* this exception occur when delete file at reading time */
			showErrorMessage(nt, "An unexpected error occurred while performing save operations. Please try again...",
					"Operation Failed");
		} catch (Exception e) {
			/* IOE Dialog box */

			/* this exception occur when delete file at reading time */
			showErrorMessage(nt, "An unexpected error occurred. Please try again...", "Operation Failed");
		}
	}

	private void bufferReader(String path, NoteIt nt) {
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {

			StringBuilder dataToRead = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				/* It is used to separate line endings */
				dataToRead.append(line).append(System.lineSeparator());
			}

			// Set the text in JTextArea at once, preserving the original structure
			nt.textArea.setText(dataToRead.toString());

			readMetaData(nt);
			if (metadata.containsKey(path)) {
				MetaData metaData = metadata.get(path);
				/* Setting the font name and font size */

				nt.textArea.setFont(new Font(metaData.getFontName(), Font.PLAIN, metaData.getFontSize()));

				/* Changing the value on GUI for Font Size, Font , WordWrap status */
				nt.fontSize = metaData.getFontSize();
				nt.itemFontSize.setText("Font Size : " + nt.fontSize);

				nt.fontName = metaData.getFontName().contains(".")
						? metaData.getFontName().substring(0, metaData.getFontName().indexOf("."))
						: metaData.getFontName();
				nt.itemFont.setText("Font : " + nt.fontName);

				if (true == metaData.isWordWrap()) {
					nt.itemWorWrap.setText("Word Wrap : On");
					nt.wrap = true;
					nt.textArea.setLineWrap(nt.wrap);
					nt.textArea.setWrapStyleWord(nt.wrap);
				}
			}

			nt.data = nt.textArea.getText();
			/* storing the length of data into global variable */
			nt.textAreaLength = nt.textArea.getText().length();
			/* Changing the frame name */
			nt.frame.setTitle(nt.fileName);
		} catch (FileNotFoundException fnf) {
			/*
			 * But we are checking for file path in open method because of that here not
			 * provided any dialogue box
			 */
		} catch (IOException io) {
			/* this exception occur when delete file at reading time */
			showErrorMessage(nt,
					"An unexpected error occurred while performing 'open'/'read' operations. Please try again.",
					"Operation Failed");
		} catch (Exception e) {
			showErrorMessage(nt, "An unexpected error occurred. Please try again.", "Operation Failed");
		}
	}

	public ActionListener exit(NoteIt nt) {

		return (ActionEvent e) -> {
			if (!isChangesDone(nt)) {
				isNeedToSave(nt).actionPerformed(e);
			} else {
				nt.frame.dispose();
			}
		};
	}

	private ActionListener isNeedToSave(NoteIt nt) {
		return (ActionEvent e) -> {
			Object[] options = { "Save", "Don't Save", "Cancel" };
			int n = JOptionPane.showOptionDialog(nt.frame, "Do you want to save changes before moving forward?",
					"Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
					options[2]);
			/* 0 means save */
			if (n == 0) {

				/* calling save so there is chance file is not saved */
				save(nt).actionPerformed(e);
				if (flag == 1) {
					/* save for new and clear screen */
					clearScreen(nt).actionPerformed(e);
					nt.frame.setTitle("Untitled");
					flag = -1;

				} else {
					/* save for exit and close */
					nt.frame.dispose();
				}
			} else if (n == 1) {
				/* Don't save option for new */
				if (flag == 1) {
					nt.textArea.setText("");
					nt.frame.setTitle("Untitled");
					flag = -1;
				} else {
					/* Don't save for close and exit */
					nt.frame.dispose();
				}
			}
		};
	}

	public ActionListener clearScreen(NoteIt nt) {
		return (ActionEvent e) -> {
			newOptn(nt).actionPerformed(e);
		};
	}

	public ActionListener displayDateAndTime(NoteIt nt) {
		return (ActionEvent e) -> {
			nt.textArea.setText(LocalDate.now().toString() + "   " + LocalTime.now().toString());
		};
	}

	public ActionListener wordWrap(NoteIt nt) {
		/* Here we can use ternary operator */
		return (ActionEvent e) -> {
			nt.itemWorWrap.setText(nt.wrap ? "Word Wrap : Off" : "Word Wrap : On");
			nt.wrap = !nt.wrap;
			nt.textArea.setLineWrap(nt.wrap);
			nt.textArea.setWrapStyleWord(nt.wrap);
		};
	}

	public ActionListener setFont(String font, NoteIt nt) {
		return (ActionEvent e) -> {
			// update the font name as well as size
			nt.fontSize = nt.textArea.getFont().getSize();
			nt.fontName = font;
			nt.textArea.setFont(new Font(font, Font.PLAIN, nt.fontSize));
			nt.itemFont.setText("Font : " + font);
		};
	}

	public ActionListener setSize(int size, NoteIt nt) {
		return (ActionEvent e) -> {
			// update the font name as well as size
			nt.fontName = nt.textArea.getFont().getFontName();
			nt.fontSize = size;
			nt.textArea.setFont(new Font(nt.fontName, Font.PLAIN, size));
			nt.itemFontSize.setText("Font Size : " + size);
		};
	}

	public ActionListener openProEditor(String path) {
		return (ActionEvent e) -> {
			// To open new window and open respective snippet file
			NoteIt nt = new NoteIt();
			// to change the icon
			Image icon = Toolkit.getDefaultToolkit()
					.getImage("dataNoteIt\\img\\ProEditor.png")
					.getScaledInstance(10000, 10000, Image.SCALE_REPLICATE);
			nt.frame.setIconImage(icon);
			// to change mode of application the theme
			nt.textArea.setBackground(new Color(0xD3D3D3));
			nt.textArea.setForeground(Color.BLACK);
			nt.textArea.setCaretColor(Color.BLACK);
			bufferReader(path, nt);
			nt.frame.setTitle("Programming Editor");
			// so the user have to save file and then cmd open on respective path or else in
			// c drive
			nt.fileName = null;
			nt.path = null;

		};
	}

	public WindowListener close(NoteIt nt) {
		return new WindowAdapter() {
			/* it will help to close with save or without save */
			public void windowClosing(WindowEvent e) {
				ActionEvent ae = new ActionEvent(e, 0, null);
				exit(nt).actionPerformed(ae);
			}
		};
	}

	public ActionListener openCMD(NoteIt nt) {
		return (ActionEvent e) -> {
			try {
				// Open the cmd at the specified path
				if (nt.fileName != null) {
					String command = "cmd.exe /c start cmd.exe /K \"cd /d " + nt.path + "\"";
					Runtime.getRuntime().exec(command);
				} else {
					// open cmd in c directory
					String command = "cmd.exe /c start cmd.exe /K \"cd /d C:\\\"";
					Runtime.getRuntime().exec(command);
				}
			} catch (IOException ioe) {
				// Handle file not found or IO exceptions
				JOptionPane.showMessageDialog(nt.frame, "Unable to open the command prompt at the specified path.",
						"Incorrect Path", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception ex) {
				// Handle any other exceptions
				showErrorMessage(nt, "An unexpected error occurred. Please try again later.", "Operation Failed");
			}
		};
	}

	private void readMetaData(NoteIt nt) {

		// Check if file exists
		File file = new File(mdFilePath);
		if (file.exists()) {

			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(mdFilePath))) {

				metadata = (LinkedHashMap<String, MetaData>) ois.readObject();

				/* Here it will throw eofException */

			} catch (EOFException e) {
			} catch (Exception e) {
				showErrorMessage(nt, "An unexpected error occurred. Please try again later.", "Operation Failed");
			}
		} else {
			showErrorMessage(nt, "MetaData file is missing", "Error");
		}
	}

	private void writeMetaData(String key, NoteIt nt) {

		try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(mdFilePath))) {
			MetaData metaData = new MetaData(nt.fontSize, nt.fontName, nt.wrap);

			metadata.put(key, metaData);
			os.writeObject(metadata);

		} catch (Exception e) {
			showErrorMessage(nt, "Something went wrong, try again...", "Error");
		}
	}

	private void showErrorMessage(NoteIt nt, String message, String title) {
		JOptionPane.showMessageDialog(nt.frame, message, title, JOptionPane.ERROR_MESSAGE);
	}

}

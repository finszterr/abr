package abr;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.JScrollBar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.ScrollPaneConstants;
import javax.swing.JList;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JCheckBox;

public class Main extends JPanel {
	private static final long serialVersionUID = 1L;

	static JFrame frame;
	JScrollPane scrollPane;
	private SwingWorker<Void, Void> listFileWorker = null;

	JPanel replacePanel = new JPanel();
	JPanel removePanel = new JPanel();
	JPanel insertPanel = new JPanel();
	JPanel regexPanel = new JPanel();
	JPanel sequencePanel = new JPanel();
	JPanel extractPanel = new JPanel();

	final static String replacePanelTitle = "Replace";
	final static String removePanelTitle = "Remove";
	final static String insertPanelTitle = "Insert";
	final static String regexPanelTitle = "RegEx";
	final static String sequencePanelTitle = "Sequence";
	final static String extractPanelTitle = "Extract";

	static String[] columnNames = { "Nr.", "File Name", "New file name", "Path", "Size", "Date Modified" };

	static Object[][] data = {};
	static DefaultTableModel model = new DefaultTableModel(data, columnNames);
	final static JTable filesTable = new JTable(model);

	private JTextField originalPatternTextField;
	private JTextField newPatternTextField;
	private JComboBox<String> replaceComboBox;
	private JComboBox<String> sequenceComboBox;

	private JTextField patternToRemoveTextField;
	private JFormattedTextField patternToInsertTextField;
	private JTextField atPositionTextField;

	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

	String lastDictionary = "";
	final static String CHOOSERTITLE = "Choose the folder ...";

	NumberFormat format = NumberFormat.getInstance();
	NumberFormatter formatter = new NumberFormatter(format);

	final static int SCROLLBARMAX = 10000;

	static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	private JTextField regexTextField;
	private JTextField newRegexTextField;
	private JTextField sequenceTextField;
	private JTextField extractStartTextField;
	private JTextField extractEndTextField;

	public Main() {
		setBackground(Color.WHITE);

		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/abr/orangeA.png")));

		// Initialize buttons (to avoid look and feel conflict with background color)
		JButton performButton = new JButton("Perform action");
		JButton clearRowsButton = new JButton("<html>&#x1F5D1");
		clearRowsButton.setToolTipText("Remove the selected rows from the table.");
		JButton simulateButton = new JButton("Simulate");

		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

		setLayout(null);

		// Loading gif
		JLabel loaderLabel = new JLabel();
		loaderLabel.setVisible(false);
		loaderLabel.setIcon(new ImageIcon(Main.class.getResource("/abr/loader.gif")));
		loaderLabel.setBounds(550, 225, 50, 50);
		add(loaderLabel);

		JFileChooser chooser = new JFileChooser(lastDictionary);

		tabbedPane.setBounds(25, 300, 730, 103);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
		add(tabbedPane);
		replacePanel.setBackground(Color.WHITE);

		tabbedPane.add(replacePanelTitle, replacePanel);
		removePanel.setBackground(Color.WHITE);
		tabbedPane.add(removePanelTitle, removePanel);
		insertPanel.setBackground(Color.WHITE);
		tabbedPane.add(insertPanelTitle, insertPanel);
		regexPanel.setBackground(Color.WHITE);
		tabbedPane.add(regexPanelTitle, regexPanel);
		sequencePanel.setBackground(Color.WHITE);
		tabbedPane.add(sequencePanelTitle, sequencePanel);
		extractPanel.setBackground(Color.WHITE);
		tabbedPane.add(extractPanelTitle, extractPanel);
		extractPanel.setLayout(null);

		JLabel extractEndLabel = new JLabel("Extract ending");
		extractEndLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		extractEndLabel.setBounds(10, 37, 128, 20);
		extractPanel.add(extractEndLabel);

		JLabel extractStartLabel = new JLabel("Extract start");
		extractStartLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		extractStartLabel.setBounds(10, 11, 128, 20);
		extractPanel.add(extractStartLabel);

		extractStartTextField = new JFormattedTextField(formatter);
		extractStartTextField.setColumns(10);
		extractStartTextField.setBounds(150, 11, 104, 20);
		extractPanel.add(extractStartTextField);

		extractEndTextField = new JFormattedTextField(formatter);
		extractEndTextField.setColumns(10);
		extractEndTextField.setBounds(150, 39, 104, 20);
		extractPanel.add(extractEndTextField);

		JLabel extractHintLabel = new JLabel(
				"<html><body>Eg. FileName.txt start=1 end=4<br>Result: File.txt</body></html>");
		extractHintLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		extractHintLabel.setBounds(372, 5, 343, 46);
		extractPanel.add(extractHintLabel);

		JCheckBox keepExtensionCheckBox = new JCheckBox("Keep extension");
		keepExtensionCheckBox.setSelected(true);
		keepExtensionCheckBox.setBackground(Color.WHITE);
		keepExtensionCheckBox.setBounds(372, 49, 196, 23);
		extractPanel.add(keepExtensionCheckBox);
		sequencePanel.setLayout(null);

		JLabel sequenceStartLabel = new JLabel("Sequence start");
		sequenceStartLabel.setBounds(10, 8, 128, 20);
		sequenceStartLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sequencePanel.add(sequenceStartLabel);

		sequenceTextField = new JTextField();
		sequenceTextField.setBounds(150, 8, 382, 20);
		sequenceTextField.setColumns(10);
		sequencePanel.add(sequenceTextField);

		sequenceComboBox = new JComboBox<>();
		sequenceComboBox.setMaximumRowCount(12);
		sequenceComboBox.setBounds(150, 34, 102, 23);
		sequenceComboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sequenceComboBox.setBackground(Color.WHITE);
		sequenceComboBox.addItem("1");
		sequenceComboBox.addItem("01");
		sequenceComboBox.addItem("001");
		sequenceComboBox.addItem(".1");
		sequenceComboBox.addItem(".01");
		sequenceComboBox.addItem(".001");
		sequenceComboBox.addItem("_1");
		sequenceComboBox.addItem("_01");
		sequenceComboBox.addItem("_001");
		sequenceComboBox.addItem("-1");
		sequenceComboBox.addItem("-01");
		sequenceComboBox.addItem("-001");

		sequencePanel.add(sequenceComboBox);

		JLabel sequenceEndLabel = new JLabel("Sequence ending");
		sequenceEndLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sequenceEndLabel.setBounds(10, 34, 128, 20);
		sequencePanel.add(sequenceEndLabel);

		regexPanel.setLayout(null);

		JLabel regexLabel = new JLabel("RegEx");
		regexLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		regexLabel.setBounds(10, 10, 93, 20);
		regexPanel.add(regexLabel);

		regexTextField = new JTextField();
		regexTextField.setColumns(10);
		regexTextField.setBounds(109, 11, 606, 20);
		regexPanel.add(regexTextField);

		JLabel newRegexLabel = new JLabel("Change to");
		newRegexLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		newRegexLabel.setBounds(10, 38, 93, 20);
		regexPanel.add(newRegexLabel);

		newRegexTextField = new JTextField();
		newRegexTextField.setColumns(10);
		newRegexTextField.setBounds(109, 39, 606, 20);
		regexPanel.add(newRegexTextField);
		replacePanel.setLayout(null);

		JLabel originalPatternLabel = new JLabel("Original pattern");
		originalPatternLabel.setBounds(10, 7, 111, 20);
		originalPatternLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		replacePanel.add(originalPatternLabel);

		JLabel newPatternLabel = new JLabel("New pattern");
		newPatternLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		newPatternLabel.setBounds(10, 34, 111, 20);
		replacePanel.add(newPatternLabel);

		originalPatternTextField = new JTextField();
		originalPatternTextField.setBounds(131, 8, 209, 20);
		replacePanel.add(originalPatternTextField);
		originalPatternTextField.setColumns(10);

		newPatternTextField = new JTextField();
		newPatternTextField.setColumns(10);
		newPatternTextField.setBounds(131, 34, 209, 20);
		replacePanel.add(newPatternTextField);

		replaceComboBox = new JComboBox<>();
		replaceComboBox.setBackground(Color.WHITE);
		replaceComboBox.setBounds(392, 8, 111, 20);
		replaceComboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		replaceComboBox.addItem("All");
		replaceComboBox.addItem("First");
		replaceComboBox.addItem("Last");
		replaceComboBox.addItem("Except first");
		replaceComboBox.addItem("Except last");

		replacePanel.add(replaceComboBox);
		removePanel.setLayout(null);

		JLabel patternToRemoveLabel = new JLabel("Original pattern");
		patternToRemoveLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		patternToRemoveLabel.setBounds(10, 8, 182, 20);
		removePanel.add(patternToRemoveLabel);

		patternToRemoveTextField = new JTextField();
		patternToRemoveTextField.setColumns(10);
		patternToRemoveTextField.setBounds(202, 11, 209, 20);
		removePanel.add(patternToRemoveTextField);

		insertPanel.setLayout(null);

		JLabel patternToInsertLabel = new JLabel("Pattern to insert");
		patternToInsertLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		patternToInsertLabel.setBounds(10, 8, 111, 20);
		insertPanel.add(patternToInsertLabel);

		JLabel atPositionLabel = new JLabel("At position");
		atPositionLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		atPositionLabel.setBounds(10, 37, 111, 20);
		insertPanel.add(atPositionLabel);

		patternToInsertTextField = new JFormattedTextField();
		patternToInsertTextField.setColumns(10);
		patternToInsertTextField.setBounds(131, 11, 209, 20);
		insertPanel.add(patternToInsertTextField);

		// Formattedtextfield restrictions
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(SCROLLBARMAX);
		formatter.setAllowsInvalid(false);
		// The value to be committed on each keystroke instead of focus lost
		formatter.setCommitsOnValidEdit(true);

		atPositionTextField = new JFormattedTextField(formatter);
		atPositionTextField.setText("0");
		atPositionTextField.setColumns(10);
		atPositionTextField.setBounds(131, 37, 105, 20);
		insertPanel.add(atPositionTextField);

		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setMaximum(SCROLLBARMAX);
		scrollBar.setValue(0);
		scrollBar.setOrientation(JScrollBar.HORIZONTAL);
		scrollBar.setBounds(255, 37, 48, 20);
		insertPanel.add(scrollBar);

		scrollBar.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				atPositionTextField.setText(String.valueOf(scrollBar.getValue()));
			}
		});

		Adjuster.adjustTableColumns(filesTable, columnNames);

		// Create buttons
		JButton addFilesButton = new JButton();
		addFilesButton.setIcon(new ImageIcon(getClass().getResource("addFiles.png")));
		addFilesButton.setBounds(25, 233, 231, 23);
		add(addFilesButton);
		addFilesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reverseLoadingVisibility(loaderLabel);

				listFileWorker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						Adder.addFiles(chooser, loaderLabel, CHOOSERTITLE, lastDictionary, sdf, model, filesTable,
								columnNames);
						return null;
					}

					/*
					 * Runs in Event Dispatch Thread (EDT)
					 */
					@Override
					protected void done() {
						// When swing worker is finished, a.k.a the heavy work, stop the gif and enable
						// the button
						reverseLoadingVisibility(loaderLabel);
						super.done();
					}
				};

				listFileWorker.execute();
			}
		});

		JButton addFilesWithSubsButton = new JButton();
		addFilesWithSubsButton.setIcon(new ImageIcon(getClass().getResource("addFilesWithSubfolders.png")));
		addFilesWithSubsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reverseLoadingVisibility(loaderLabel);

				listFileWorker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						Adder.addFilesWithSubs(chooser, CHOOSERTITLE, lastDictionary, sdf, model, filesTable,
								columnNames);
						return null;
					}

					/*
					 * Runs in Event Dispatch Thread (EDT)
					 */
					@Override
					protected void done() {
						// When swing worker is finished, a.k.a the heavy work, stop the gif and enable
						// the button
						reverseLoadingVisibility(loaderLabel);
						super.done();
					}
				};

				listFileWorker.execute();
			}
		});
		addFilesWithSubsButton.setBounds(25, 258, 231, 23);
		add(addFilesWithSubsButton);

		designButton(simulateButton, new Color(1, 162, 250), new Color(1, 225, 250), 14);
		simulateButton.setBounds(25, 421, 110, 36);
		add(simulateButton);
		simulateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Simulate.simulateRenaming(tabbedPane, originalPatternTextField, newPatternTextField, model,
						replaceComboBox, patternToRemoveTextField, patternToInsertTextField, regexTextField,
						newRegexTextField, atPositionTextField, sequenceTextField, sequenceComboBox,
						extractStartTextField, extractEndTextField, keepExtensionCheckBox, filesTable, columnNames);
			}
		});

		JButton clearButton = new JButton("Clear list");
		clearButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		clearButton.setToolTipText("Remove all rows from the table.");
		clearButton.setBounds(911, 233, 89, 25);
		add(clearButton);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Deleter.deleteAllRows(model);
			}
		});

		designButton(performButton, new Color(105, 192, 103), new Color(105, 208, 153), 14);
		performButton.setBounds(162, 421, 164, 36);
		add(performButton);

		performButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Performer.performRenaming(loaderLabel, filesTable, model, columnNames);
			}
		});

		JButton addFoldersButton = new JButton();
		addFoldersButton.setIcon(new ImageIcon(getClass().getResource("addFolders.png")));
		addFoldersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Adder.addDirectories(chooser, CHOOSERTITLE, lastDictionary, model, sdf, filesTable, columnNames);
			}
		});

		addFoldersButton.setBounds(289, 233, 231, 23);
		add(addFoldersButton);

		JButton addFoldersWithSubsButton = new JButton();
		addFoldersWithSubsButton.setIcon(new ImageIcon(getClass().getResource("addFoldersWithSubfolders.png")));
		addFoldersWithSubsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Adder.addDirectoriesWithSubs(chooser, CHOOSERTITLE, lastDictionary, model, sdf, filesTable,
						columnNames);
			}
		});
		addFoldersWithSubsButton.setBounds(289, 258, 231, 23);
		add(addFoldersWithSubsButton);

		clearRowsButton.setFont(new Font("Tahoma", Font.PLAIN, 24));
		clearRowsButton.setBounds(1010, 233, 50, 25);
		add(clearRowsButton);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 1050, 211);
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		filesTable.setColumnSelectionAllowed(true);
		filesTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		filesTable.setFillsViewportHeight(true);
		filesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// Avoid remain changed cell bug when deleting row
		filesTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		filesTable.getColumnModel().setColumnMargin(10);

		// Create the scroll pane and add the table to it.
		scrollPane = new JScrollPane(filesTable);
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setPreferredSize(new Dimension(700, 400));
		panel.add(scrollPane, BorderLayout.CENTER);

		JScrollPane regexHintsScrollPane = new JScrollPane();
		regexHintsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		regexHintsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		regexHintsScrollPane.setBounds(769, 269, 291, 188);
		add(regexHintsScrollPane);

		DefaultListModel<String> listModel = new DefaultListModel<>();
		// Wrap the text in the list if needed
		MyCellRenderer cellRenderer = new MyCellRenderer(210);
		listRegexHints(listModel);

		JList<String> regexHintsList = new JList<>(listModel);
		regexHintsList.setCellRenderer(cellRenderer);

		regexHintsScrollPane.setViewportView(regexHintsList);

		// Set hints invisible by default
		regexHintsScrollPane.setVisible(false);
		// Set visible regex hints if needed
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (tabbedPane.getSelectedIndex() == 3) {
					regexHintsScrollPane.setVisible(true);
				} else {
					regexHintsScrollPane.setVisible(false);
				}
			}
		});

		clearRowsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Deleter.deleteRows(filesTable, model);
			}
		});
	}

	void listRegexHints(DefaultListModel<String> listModel) {
		// Common matching symbols
		listModel.addElement("<html><b><i>Common matching symbols</i></b></html>");
		listModel.addElement("<html><b>.</b></html>");
		listModel.addElement("Matches any character");

		listModel.addElement("<html><b>^regex</b></html>");
		listModel.addElement("Finds regex that must match at the beginning of the line.");

		listModel.addElement("<html><b>regex$</b></html>");
		listModel.addElement("Finds regex that must match at the end of the line.");

		listModel.addElement("<html><b>[abc]</b></html>");
		listModel.addElement("Set definition, can match the letter a or b or c.");

		listModel.addElement("<html><b>[abc][vz]</b></html>");
		listModel.addElement("Set definition, can match a or b or c followed by either v or z.");

		listModel.addElement("<html><b>[^abc]</b></html>");
		listModel.addElement(
				"When a caret appears as the first character inside square brackets, it negates the pattern. This pattern matches any character except a or b or c.");

		listModel.addElement("<html><b>[a-d1-7]</b></html>");
		listModel.addElement("Ranges: matches a letter between a and d and figures from 1 to 7, but not d1.");

		listModel.addElement("<html><b>X|Z</b></html>");
		listModel.addElement("Finds X or Z.");

		listModel.addElement("<html><b>XZ</b></html>");
		listModel.addElement("Finds X directly followed by Z.");

		listModel.addElement("<html><b>$</b></html>");
		listModel.addElement("Checks if a line end follows.");

		// Common matching symbols
		listModel.addElement("<html><b><i>Meta characters</i></b></html>");
		listModel.addElement("<html><b>\\d</b></html>");
		listModel.addElement("Any digit, short for [0-9]");

		listModel.addElement("<html><b>\\D</b></html>");
		listModel.addElement("A non-digit, short for [^0-9]");

		listModel.addElement("<html><b>\\s</b></html>");
		listModel.addElement("A whitespace character, short for [ \\t\\n\\x0b\\r\\f]");

		listModel.addElement("<html><b>\\S</b></html>");
		listModel.addElement("A non-whitespace character, short for");

		listModel.addElement("<html><b>\\w</b></html>");
		listModel.addElement("A word character, short for [a-zA-Z_0-9]");

		listModel.addElement("<html><b>\\W</b></html>");
		listModel.addElement("A non-word character [^\\w]");

		listModel.addElement("<html><b>\\S+</b></html>");
		listModel.addElement("Several non-whitespace characters");

		listModel.addElement("<html><b>\\b</b></html>");
		listModel.addElement("Matches a word boundary where a word character is [a-zA-Z0-9_]");

		// Quantifier
		listModel.addElement("<html><b><i>Quantifier</i></b></html>");

		listModel.addElement("<html><b>*</b></html>");
		listModel.addElement("Occurs zero or more times, is short for {0,}");

		listModel.addElement("<html><b>+</b></html>");
		listModel.addElement("Occurs one or more times, is short for {1,}");

		listModel.addElement("<html><b>?</b></html>");
		listModel.addElement("Occurs no or one times, ? is short for {0,1}.");

		listModel.addElement("<html><b>{X}</b></html>");
		listModel.addElement("Occurs X number of times, {} describes the order of the preceding liberal");

		listModel.addElement("<html><b>{X,Y}</b></html>");
		listModel.addElement("Occurs between X and Y times,");

		listModel.addElement("<html><b>*?</b></html>");
		listModel.addElement(
				"? after a quantifier makes it a reluctant quantifier. It tries to find the smallest match. This makes the regular expression stop at the first match.");
	}

	private void reverseLoadingVisibility(JLabel loaderLabel) {
		loaderLabel.setVisible(!loaderLabel.isVisible());
	}

	private void designButton(JButton b, Color backgroundColor, Color hoverBackgroundColor, int fontSize) {
		b.setFocusPainted(false);
		b.setBackground(backgroundColor);
		b.setForeground(Color.WHITE);

		b.setFont(new Font("Tahoma", Font.BOLD, fontSize));

		// Hover background
		b.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				b.setBackground(hoverBackgroundColor);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				b.setBackground(backgroundColor);
			}
		});
	}

	private static void createAndShowGUI() {
		// Create and set up the window.
		frame = new JFrame("Advanced Bulk Renamer");
		// Create and set up the content pane.
		Main newContentPane = new Main();
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.setSize(1100, 515);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
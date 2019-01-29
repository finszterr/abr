package abr;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Simulate {
	public static void simulateRenaming(JTabbedPane tabbedPane, JTextField originalPatternTextField, JTextField newPatternTextField,
			DefaultTableModel model, JComboBox<String> replaceComboBox, JTextField patternToRemoveTextField, 
			JFormattedTextField patternToInsertTextField, JTextField regexTextField, JTextField newRegexTextField,
			JTextField atPositionTextField, JTextField sequenceTextField, JComboBox<String> sequenceComboBox,
			JTextField extractStartTextField, JTextField extractEndTextField, JCheckBox keepExtensionCheckBox,
			JTable filesTable, String[] columnNames
			) {
		int selectedTab = tabbedPane.getSelectedIndex();

		// Replace
		if (selectedTab == 0) {
			String originalPattern = originalPatternTextField.getText();
			String newPattern = newPatternTextField.getText();

			int i = 0;
			for (; i < model.getRowCount(); i++) {
				String oldName = (String) model.getValueAt(i, 1);

				String replaceMode = replaceComboBox.getSelectedItem().toString();
				String newName = null;

				if (replaceMode.equals("All")) {
					newName = oldName.replace(originalPattern, newPattern);

				} else if (replaceMode.equals("First")) {
					newName = replaceFirst(oldName, originalPattern, newPattern);

				} else if (replaceMode.equals("Last")) {
					newName = replaceLast(oldName, originalPattern, newPattern);

				} else if (replaceMode.equals("Except first")) {
					newName = oldName.replace(originalPattern, newPattern);
					newName = replaceFirst(newName, newPattern, originalPattern);

				} else if (replaceMode.equals("Except last")) {
					newName = oldName.replace(originalPattern, newPattern);
					newName = replaceLast(newName, newPattern, originalPattern);

				}

				model.setValueAt(newName, i, 2);
			}

		}

		// Remove
		if (selectedTab == 1) {
			String pattern = patternToRemoveTextField.getText();

			int i = 0;
			for (; i < model.getRowCount(); i++) {
				String oldName = (String) model.getValueAt(i, 1);
				String newName = oldName.replaceAll(pattern, "");

				model.setValueAt(newName, i, 2);
			}

		}

		// Insert
		if (selectedTab == 2) {
			String pattern = patternToInsertTextField.getText();

			int i = 0;
			for (; i < model.getRowCount(); i++) {
				String oldName = (String) model.getValueAt(i, 1);
				int position = Math.min(Integer.parseInt(atPositionTextField.getText().replaceAll(",", "")),
						oldName.length());

				StringBuilder newName = new StringBuilder(oldName).insert(position, pattern);

				model.setValueAt(newName, i, 2);
			}

		}

		// RegEx
		if (selectedTab == 3) {
			String pattern = regexTextField.getText();
			String newPattern = newRegexTextField.getText();

			int e = 0;
			for (; e < model.getRowCount(); e++) {
				String oldName = (String) model.getValueAt(e, 1);
				String newName = oldName.replaceAll(pattern, newPattern);

				model.setValueAt(newName, e, 2);
			}

		}

		// Sequence
		if (selectedTab == 4) {
			String startPattern = sequenceTextField.getText();
			String endingPattern = (String) sequenceComboBox.getSelectedItem();

			int e = 0;
			for (; e < model.getRowCount(); e++) {
				String oldName = (String) model.getValueAt(e, 1);

				// "1"
				if (endingPattern.equals(sequenceComboBox.getItemAt(0))
						|| endingPattern.equals(sequenceComboBox.getItemAt(3))
						|| endingPattern.equals(sequenceComboBox.getItemAt(6))
						|| endingPattern.equals(sequenceComboBox.getItemAt(9))) {
					// Check if there is extension or not
					String extensionWithDot = "";
					String beforeNumbersSign = "";

					if (endingPattern.equals(sequenceComboBox.getItemAt(0))) {

					} else if (endingPattern.equals(sequenceComboBox.getItemAt(3))) {
						beforeNumbersSign = ".";
					} else if (endingPattern.equals(sequenceComboBox.getItemAt(6))) {
						beforeNumbersSign = "_";
					} else {
						beforeNumbersSign = "-";
					}

					if (oldName.lastIndexOf('.') != -1) {
						extensionWithDot = oldName.substring(oldName.lastIndexOf('.'));
					}
					String newName = startPattern + beforeNumbersSign + (e + 1) + extensionWithDot;

					model.setValueAt(newName, e, 2);

				}

				// "01"
				if (endingPattern.equals(sequenceComboBox.getItemAt(1))
						|| endingPattern.equals(sequenceComboBox.getItemAt(4))
						|| endingPattern.equals(sequenceComboBox.getItemAt(7))
						|| endingPattern.equals(sequenceComboBox.getItemAt(10))) {
					// Check if there is extension or not
					String extensionWithDot = "";
					String beforeNumbersSign = "";

					if (endingPattern.equals(sequenceComboBox.getItemAt(1))) {

					} else if (endingPattern.equals(sequenceComboBox.getItemAt(4))) {
						beforeNumbersSign = ".";
					} else if (endingPattern.equals(sequenceComboBox.getItemAt(7))) {
						beforeNumbersSign = "_";
					} else {
						beforeNumbersSign = "-";
					}

					if (oldName.lastIndexOf('.') != -1) {
						extensionWithDot = oldName.substring(oldName.lastIndexOf('.'));
					}
					// Check if 01-09 is the element or bigger
					if (e + 1 < 10) {
						String newName = startPattern + beforeNumbersSign + "0" + (e + 1) + extensionWithDot;
						model.setValueAt(newName, e, 2);
					} else {
						String newName = startPattern + beforeNumbersSign + (e + 1) + extensionWithDot;
						model.setValueAt(newName, e, 2);
					}

				}

				// "001"
				if (endingPattern.equals(sequenceComboBox.getItemAt(2))
						|| endingPattern.equals(sequenceComboBox.getItemAt(5))
						|| endingPattern.equals(sequenceComboBox.getItemAt(8))
						|| endingPattern.equals(sequenceComboBox.getItemAt(11))) {
					// Check if there is extension or not
					String extensionWithDot = "";
					String beforeNumbersSign = "";

					if (endingPattern.equals(sequenceComboBox.getItemAt(2))) {

					} else if (endingPattern.equals(sequenceComboBox.getItemAt(5))) {
						beforeNumbersSign = ".";
					} else if (endingPattern.equals(sequenceComboBox.getItemAt(8))) {
						beforeNumbersSign = "_";
					} else {
						beforeNumbersSign = "-";
					}

					if (oldName.lastIndexOf('.') != -1) {
						extensionWithDot = oldName.substring(oldName.lastIndexOf('.'));
					}
					// Check if 01-09 is the element or bigger
					if (e + 1 < 10) {
						String newName = startPattern + beforeNumbersSign + "00" + (e + 1) + extensionWithDot;
						model.setValueAt(newName, e, 2);

					} else if (e + 1 < 100) {
						// Check if 010-099 is the element or bigger
						String newName = startPattern + beforeNumbersSign + "0" + (e + 1) + extensionWithDot;
						model.setValueAt(newName, e, 2);

					} else {
						// Check if 100 is the element or bigger
						String newName = startPattern + beforeNumbersSign + (e + 1) + extensionWithDot;
						model.setValueAt(newName, e, 2);
					}

				}
			}

		}
		
		// Extract
		if (selectedTab == 5) {
			
			int start = 0;
			int end = 0;
			//Avoid 0 error
			try {
				start = Integer.parseInt(extractStartTextField.getText()) -1;
			} catch (Exception e2) {
				System.out.println("Start is less than 1.");
			}
			
			try {
				end = Integer.parseInt(extractEndTextField.getText()) -1;
			} catch (Exception e2) {
				System.out.println("End is less than 1.");
			}

			if(start>=end+1) return;
			
			int e = 0;
			for (; e < model.getRowCount(); e++) {
				String oldName = (String) model.getValueAt(e, 1);
				String extension = "";
				//Get extension
				if(keepExtensionCheckBox.isSelected()) extension = oldName.substring(oldName.lastIndexOf('.'));
				//Avoid duplicate extension
								
				String newName = oldName.substring(start, end+1) + extension;
				model.setValueAt(newName, e, 2);
			}
		}
		// Readjust columns
		Adjuster.adjustTableColumns(filesTable, columnNames);

	}
	
	public static String replaceFirst(String string, String from, String to) {
		int firstIndex = string.indexOf(from);
		if (firstIndex < 0)
			return string;
		String head = string.substring(firstIndex).replaceFirst(from, to);
		return string.substring(0, firstIndex) + head;
	}

	public static String replaceLast(String string, String from, String to) {
		int lastIndex = string.lastIndexOf(from);
		if (lastIndex < 0)
			return string;
		String tail = string.substring(lastIndex).replaceFirst(from, to);
		return string.substring(0, lastIndex) + tail;
	}
}

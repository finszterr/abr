package abr;

import java.io.File;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Performer {
	public static void performRenaming(JLabel loaderLabel, JTable filesTable, DefaultTableModel model, String[] columnNames) {
		int row = 0;
		int successes = 0;

		for (; row < filesTable.getRowCount(); row++) {
			File file = new File(model.getValueAt(row, 3) + "\\" + model.getValueAt(row, 1));
			File newFile = new File(model.getValueAt(row, 3) + "\\" + model.getValueAt(row, 2));
			if (file.renameTo(newFile)) {
				System.out.println("File rename success");
				successes++;
				updateFileNames(row, model);
			} else {
				System.out.println("File rename failed");
			}

		}
		System.out.println("successes: " + successes + " / " + row);
		JOptionPane.showMessageDialog(null, "Successfully renamed: " + successes + " / " + row, "Renaming",
				JOptionPane.INFORMATION_MESSAGE);

		Adjuster.adjustTableColumns(filesTable, columnNames);
	}
	
	private static void updateFileNames(int row, DefaultTableModel model) {
		model.setValueAt(model.getValueAt(row, 2), row, 1);
		model.setValueAt(null, row, 2);
	}
}

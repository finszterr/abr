package abr;

import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Deleter {
	public static void deleteRows(JTable filesTable, DefaultTableModel model) {
		int[] selectedRows = filesTable.getSelectedRows();
		Arrays.sort(selectedRows);

		// Remove rows in reverse order ! (after remove stay consistent)
		for (int i = selectedRows.length - 1; i >= 0; i--) {
			model.removeRow(selectedRows[i]);
		}

		// Adjust Nr.-s
		int row = 0;
		for (; row < filesTable.getRowCount(); row++) {
			model.setValueAt(row + 1, row, 0);
		}

	}

	public static void deleteAllRows(DefaultTableModel model) {
		int i = model.getRowCount() - 1;
		for (; i >= 0; i--) {
			model.removeRow(i);
		}
	}
}

package abr;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class Adjuster {
	public static void adjustTableColumns(JTable filesTable, String[] columnNames) {
		// Adjust the column width according to the text length
		for (int column = 0; column < filesTable.getColumnCount(); column++) {
			TableColumn tableColumn = filesTable.getColumnModel().getColumn(column);
			// Minimum of column title or rows
			int preferredWidth = Math.max(columnNames[column].length() * 9, tableColumn.getMinWidth());

			int maxWidth = tableColumn.getMaxWidth();

			for (int row = 0; row < filesTable.getRowCount(); row++) {
				TableCellRenderer cellRenderer = filesTable.getCellRenderer(row, column);
				Component c = filesTable.prepareRenderer(cellRenderer, row, column);
				int width = c.getPreferredSize().width + filesTable.getIntercellSpacing().width;
				preferredWidth = Math.max(preferredWidth, width);

				// We've exceeded the maximum width, no need to check other rows
				if (preferredWidth >= maxWidth) {
					preferredWidth = maxWidth;
					break;
				}
			}

			tableColumn.setPreferredWidth(preferredWidth);
		}
	}
}

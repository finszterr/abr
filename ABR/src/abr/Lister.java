package abr;

import java.io.File;
import java.text.SimpleDateFormat;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Lister {
	public static void listf(String directoryName, String m, JTable filesTable, DefaultTableModel model, 
			SimpleDateFormat sdf) {
		// Declaring the file and passing directory path as parameter
		File directory = new File(directoryName);

		// Get all the files from a directory
		File[] fList = directory.listFiles();
		try {
			for (File file : fList) {
				if (m.equals("files")) {
					if (file.isFile()) {
						// Avoid duplicates
						int row = 0;
						boolean duplicate = false;
						for (; row < filesTable.getRowCount(); row++) {
							if (model.getValueAt(row, 1).equals(file.getName())
									&& model.getValueAt(row, 3).equals(file.getParent())) {
								duplicate = true;
								break;
							}
						}

						if (!duplicate) {
							model.addRow(
									new Object[] { filesTable.getRowCount() + 1, file.getName(), null, file.getParent(),
											file.length() / 1024 + " KB", sdf.format((file).lastModified()) });
						}
					} else if (file.isDirectory()) {
						// passing the path again into function to print the files in sub directory
						listf(file.getPath(), "directories", filesTable, model, sdf); // using recursion function
					}
				}

				if (m.equals("directories")) {
					if (file.isDirectory()) {
						// Avoid duplicates
						int row = 0;
						boolean duplicate = false;
						for (; row < filesTable.getRowCount(); row++) {
							if (model.getValueAt(row, 1).equals(file.getName())
									&& model.getValueAt(row, 3).equals(file.getParent())) {
								duplicate = true;
								break;
							}
						}
						if (!duplicate) {
							model.addRow(new Object[] { filesTable.getRowCount() + 1, file.getName(), null,
									file.getParent(), "-", sdf.format((file).lastModified()) });
						}

						// passing the path again into function to print the files in sub directory
						listf(file.getPath(), "directories", filesTable, model, sdf); // using recursion function
					}

				}

			}
		} catch (NullPointerException npe) {
			System.out.println("directory/file not found");
		}
	}

	public static void listfOnlySelectedFolder(String directoryName, String m, JTable filesTable, DefaultTableModel model, 
			SimpleDateFormat sdf) {
		// Declaring the file and passing directory path as parameter
		File directory = new File(directoryName);

		// Get all the files from a directory
		File[] fList = directory.listFiles();
		try {

			for (File file : fList) {
				if (m.equals("files")) {
					if (file.isFile()) {
						// Avoid duplicates
						int row = 0;
						boolean duplicate = false;
						for (; row < filesTable.getRowCount(); row++) {
							if (model.getValueAt(row, 1).equals(file.getName())
									&& model.getValueAt(row, 3).equals(file.getParent())) {
								duplicate = true;
								break;
							}
						}

						if (!duplicate) {
							model.addRow(
									new Object[] { filesTable.getRowCount() + 1, file.getName(), null, file.getParent(),
											file.length() / 1024 + " KB", sdf.format((file).lastModified()) });
						}
					} else if (file.isDirectory()) {
						// Not passing the path again into function to print the files in sub directory
					}
				}

				if (m.equals("directories")) {
					if (file.isDirectory()) {

						// Avoid duplicates
						int row = 0;
						boolean duplicate = false;
						for (; row < filesTable.getRowCount(); row++) {
							if (model.getValueAt(row, 1).equals(file.getName())
									&& model.getValueAt(row, 3).equals(file.getParent())) {
								duplicate = true;
								break;
							}

						}

						if (!duplicate) {
							model.addRow(new Object[] { filesTable.getRowCount() + 1, file.getName(), null,
									file.getParent(), "-", sdf.format((file).lastModified()) });

						}
						// Doesen't need recursive function here as well
					}

				}

			}

		} catch (NullPointerException npe) {
			System.out.println("directory/file not found");
		}

	}

}

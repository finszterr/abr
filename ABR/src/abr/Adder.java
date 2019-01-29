package abr;

import java.io.File;
import java.text.SimpleDateFormat;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Adder {
	public static void addFiles(JFileChooser chooser, JLabel loaderLabel, String CHOOSERTITLE, String lastDictionary,
		SimpleDateFormat sdf,	DefaultTableModel model ,JTable filesTable, String[] columnNames) {
		chooser.setDialogTitle(CHOOSERTITLE);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(true);

		if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) {
			// create a file that is really a directory
			File aDirectory = new File(chooser.getSelectedFile().toString());
			lastDictionary = chooser.getSelectedFile().toString();

			Lister.listfOnlySelectedFolder(String.valueOf(aDirectory), "files", filesTable, model, sdf);
		}
		// Readjust columns
		Adjuster.adjustTableColumns(filesTable, columnNames);

	}

	public static void addFilesWithSubs(JFileChooser chooser, String CHOOSERTITLE, String lastDictionary, SimpleDateFormat sdf,
			DefaultTableModel model, JTable filesTable, String[] columnNames) {
		chooser.setDialogTitle(CHOOSERTITLE);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(true);

		if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) {
			// create a file that is really a directory
			File aDirectory = new File(chooser.getSelectedFile().toString());
			lastDictionary = chooser.getSelectedFile().toString();

			Lister.listf(String.valueOf(aDirectory), "files", filesTable, model, sdf); 
		}
		// Readjust columns
		Adjuster.adjustTableColumns(filesTable, columnNames);
	}

	public static void addDirectories(JFileChooser chooser, String CHOOSERTITLE, String lastDictionary, DefaultTableModel model,
			SimpleDateFormat sdf, JTable filesTable, String[] columnNames) {
		chooser.setDialogTitle(CHOOSERTITLE);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(true);

		if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) {
			// create a file that is really a directory
			File aDirectory = new File(chooser.getSelectedFile().toString());
			lastDictionary = chooser.getSelectedFile().toString();

			// get a listing of all files in the directory
			String[] filesInDir = aDirectory.list();
			System.out.println("Number of files: " + filesInDir.length);

			for (int i = 0; i < filesInDir.length; i++) {
				File currentFile = new File(aDirectory + "\\" + filesInDir[i]);

				// Avoid duplicates
				int row = 0;
				boolean duplicate = false;
				for (; row < filesTable.getRowCount(); row++) {
					if (model.getValueAt(row, 1).equals(filesInDir[i]) && model.getValueAt(row, 3).equals(aDirectory)) {
						duplicate = true;
						break;
					}
				}

				if (!duplicate && currentFile.isDirectory()) {
					model.addRow(new Object[] { filesTable.getRowCount() + 1, filesInDir[i], null, aDirectory, "-",
							sdf.format((currentFile).lastModified()) });
				}
			}
		} else {
			System.out.println("No Selection ");
		}

		// Readjust columns
		Adjuster.adjustTableColumns(filesTable, columnNames);
	}

	public static void addDirectoriesWithSubs(JFileChooser chooser, String CHOOSERTITLE, String lastDictionary,	
			DefaultTableModel model, SimpleDateFormat sdf, JTable filesTable, String[] columnNames) {
		chooser.setDialogTitle(CHOOSERTITLE);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(true);

		if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) {
			// create a file that is really a directory
			File aDirectory = new File(chooser.getSelectedFile().toString());
			lastDictionary = chooser.getSelectedFile().toString();

			Lister.listf(String.valueOf(aDirectory), "directories", filesTable, model, sdf); 
			
		}
		// Readjust columns
		Adjuster.adjustTableColumns(filesTable, columnNames);
	}
}

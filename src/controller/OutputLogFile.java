package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OutputLogFile {
	File newfile;
	String path;

	public OutputLogFile(String path) {
		// TODO Auto-generated constructor stub
		this.path = path;
		System.out.println("output " + path);

		init();
	}

	public void init() {
		newfile = new File(path);
		if (!newfile.exists()) {
			try {
				newfile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				FileWriter filewriter = new FileWriter(newfile, true);

				filewriter.write("\r\n");
				filewriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void write(String text) {
		StringBuffer sb = new StringBuffer();
		try {
			FileWriter filewriter = new FileWriter(newfile, true);

			Calendar cl = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd-HH:mm:ss] ");

			sb.append(sdf.format(cl.getTime()));
			sb.append(text);
			sb.append("\r\n");

			filewriter.write(sb.toString());

			filewriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void write(String text, String name) {
		StringBuffer sb = new StringBuffer();
		sb.append(text);
		sb.append(name);
		write(sb.toString());
	}
}

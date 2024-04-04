package me.deadorfd.videos.utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils
 * @Date 25.12.2022
 * @Time 00:37:02
 */
public class Utils {

	public static boolean isCreatedInTheLastDays(File file, int time) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date videodate = null;
		try {
			videodate = sdf.parse(sdf.format(file.lastModified()));
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		Calendar thirtyDaysAgo = Calendar.getInstance();
		thirtyDaysAgo.add(Calendar.DAY_OF_MONTH, -time);
		Date thirtyDaysAgoDate = thirtyDaysAgo.getTime();
		return videodate.after(thirtyDaysAgoDate);
	}

	public static void openImageFrame(String path) {
		JFrame frame = new JFrame(); // creates jframe
		File file = new File(path);
		frame.setTitle(file.getName());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setUndecorated(false);
		ImageIcon image = new ImageIcon(path);

		JLabel lbl = new JLabel(image);

		frame.getContentPane().add(lbl);
		frame.setSize(image.getIconWidth(), image.getIconHeight());

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				File file = new File(path);
				file.delete();
			}
		});
	}

	public static URL getResource(String name) {
		return Utils.class.getResource(name);
	}

	public static String getPathIfFileDoesntExists(File video, String path) {
		for (File file : new File(path).listFiles()) {
			if (file.getName().equalsIgnoreCase(video.getName())) {
				return file.getPath().replace('\\', '/');
			} else if (file.isDirectory()) {
				String returnVal = getPathIfFileDoesntExists(video, file.getPath());
				if (returnVal != null) return returnVal;
			}
		}
		return null;
	}

}
package me.deadorfd.videos.utils;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

	public static JPanel openImageFrameVideo(String path, JPanel panel) {
		File file = new File(path);
		ImageIcon image = getScaledImage(path, panel.getWidth(), panel.getHeight());
		JLabel lbl = new JLabel(image); // puts the image into a jlabel
		panel.add(lbl); // puts label inside the jframe
		return panel;
	}

	private static ImageIcon getScaledImage(String path, int width, int height) {
		Image srcImg = new ImageIcon(path).getImage();
		BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, width, height, null);
		g2.dispose();
		return new ImageIcon(resizedImg);
	}

	public static Icon getScaledImage1(String imgName, int width, int height) {
		Image srcImg = new ImageIcon(getResource("/images/" + imgName)).getImage();
		BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, width, height, null);
		g2.dispose();

		return new ImageIcon(resizedImg);
	}

	public static URL getResource(String name) {
		return Utils.class.getResource(name);
	}
}
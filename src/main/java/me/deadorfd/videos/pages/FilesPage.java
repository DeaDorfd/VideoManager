package me.deadorfd.videos.pages;

import static me.deadorfd.videos.utils.Data.*;
import static me.deadorfd.videos.utils.FileManager.*;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import me.deadorfd.videos.Main;
import me.deadorfd.videos.manager.WrapLayout;
import me.deadorfd.videos.utils.Data;
import me.deadorfd.videos.utils.Folder;
import me.deadorfd.videos.utils.Utils;
import me.deadorfd.videos.utils.video.NormalVideo;
import me.deadorfd.videos.utils.video.info.NormalVideoInfo;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.pages
 * @Date 20.12.2022
 * @Time 15:00:41
 */
public class FilesPage extends JPanel {

	/**
	 * Create the panel.
	 */

	public FilesPage() {
		setBackground(new Color(128, 128, 128));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBounds(100, 100, 1280, 720);
		setLayout(null);

		JLabel titel = new JLabel("Files");
		titel.setFont(new Font(font, Font.PLAIN, 15));
		titel.setHorizontalAlignment(SwingConstants.CENTER);
		titel.setBounds(219, 11, 120, 14);
		add(titel);

		JButton btnFileButton = new JButton("Datei");
		btnFileButton.setFont(new Font(font, Font.PLAIN, 11));
		btnFileButton.addActionListener(e -> {
			try {
				Desktop.getDesktop().open(new File("E:\\Videos\\Videos\\usasmoking.txt"));
			} catch (IOException e1) {}
		});
		btnFileButton.setBounds(10, 9, 89, 23);
		add(btnFileButton);

		JButton btnResetButton = new JButton("Reset");
		btnResetButton.setFont(new Font(font, Font.PLAIN, 11));
		btnResetButton.setBounds(109, 9, 89, 23);
		btnResetButton.addActionListener(e -> {
			path = oripath;
			currentFolders.clear();
			reinit();
		});
		add(btnResetButton);

		JButton btnbackButton = new JButton("Back");
		btnbackButton.setFont(new Font(font, Font.PLAIN, 15));
		btnbackButton.addActionListener(event -> {
			if (!currentFolders.isEmpty()) {
				currentFolders.remove(currentFolders.size() - 1);
				updatePath();
				reinit();
			}
		});
		btnbackButton.setBounds(317, 642, 89, 23);
		add(btnbackButton);

		JButton btnMainButton = new JButton("Menü");
		btnMainButton.addActionListener(e -> Main.switchScenes("main"));
		btnMainButton.setFont(new Font(font, Font.PLAIN, 15));
		btnMainButton.setBounds(187, 642, 120, 23);
		add(btnMainButton);

		JButton btnOpenFolder = new JButton("Ordner Öffnen");
		btnOpenFolder.addActionListener(e -> {
			try {
				Desktop.getDesktop().open(new File(path));
			} catch (IOException e1) {}
		});
		btnOpenFolder.setFont(new Font(font, Font.PLAIN, 15));
		btnOpenFolder.setBounds(10, 642, 167, 23);
		add(btnOpenFolder);

		JScrollPane scrollPane_Videos = new JScrollPane();
		scrollPane_Videos.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane_Videos.setBounds(20, 53, 237, 256);
		add(scrollPane_Videos);

		panel_Videos = new JPanel();
		panel_Videos.setLayout(new WrapLayout());
		panel_Videos.setSize(new Dimension(300, 1));
		scrollPane_Videos.setViewportView(panel_Videos);
		for (NormalVideo video : videos) panel_Videos.add(getVideoButton(video));

		JScrollPane scrollPane_Folders = new JScrollPane();
		scrollPane_Folders.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane_Folders.setBounds(20, 320, 237, 311);
		add(scrollPane_Folders);

		panel_Folders = new JPanel();
		panel_Folders.setLayout(new WrapLayout());
		panel_Folders.setSize(new Dimension(300, 1));
		scrollPane_Folders.setViewportView(panel_Folders);
		for (File folder : folders) panel_Folders.add(getFolderButton(new Folder(folder)));

		scrollPane_VideoInfo = new JScrollPane();
		scrollPane_VideoInfo.setBounds(401, 53, 743, 578);
		scrollPane_VideoInfo.setVisible(false);
		add(scrollPane_VideoInfo);

		panel_VideoInfo = new JPanel();
		panel_VideoInfo.setBackground(Color.DARK_GRAY);
		panel_VideoInfo.setLayout(null);
		panel_VideoInfo.setSize(new Dimension(300, 1));
		scrollPane_VideoInfo.setViewportView(panel_VideoInfo);

		lblVideoTitel = new JLabel("Video:");
		lblVideoTitel.setForeground(Color.WHITE);
		lblVideoTitel.setHorizontalAlignment(SwingConstants.CENTER);
		lblVideoTitel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblVideoTitel.setBounds(10, 27, 721, 33);
		panel_VideoInfo.add(lblVideoTitel);

		panel_VideoImage = new JPanel();
		panel_VideoImage.setBounds(152, 68, 436, 231);
		panel_VideoInfo.add(panel_VideoImage);

		btnPlay = new JButton("Video Abspielen");
		btnPlay.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnPlay.setBounds(242, 516, 247, 33);
		panel_VideoInfo.add(btnPlay);

		btnFav = new JButton("Favorisieren");
		btnFav.setBounds(625, 542, 106, 23);
		panel_VideoInfo.add(btnFav);

		textArea_VideoInfo = new JTextArea();
		textArea_VideoInfo.setFont(new Font("Monospaced", Font.BOLD, 16));
		textArea_VideoInfo.setEditable(false);
		textArea_VideoInfo.setLineWrap(true);
		textArea_VideoInfo.setBounds(242, 357, 247, 133);
		panel_VideoInfo.add(textArea_VideoInfo);

		lblVideoInfo = new JLabel("Video Infos:");
		lblVideoInfo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblVideoInfo.setForeground(Color.WHITE);
		lblVideoInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblVideoInfo.setBounds(242, 299, 247, 47);
		panel_VideoInfo.add(lblVideoInfo);

	}

	private JPanel panel_Folders;
	private JPanel panel_Videos;

	private JScrollPane scrollPane_VideoInfo;
	private JPanel panel_VideoInfo;
	private JPanel panel_VideoImage;
	private JLabel lblVideoTitel;
	private JButton btnPlay;
	private JButton btnFav;
	private JTextArea textArea_VideoInfo;
	private JLabel lblVideoInfo;
	private String lastImg;

	private void openVideoInfo(NormalVideo video) {
		if (video.isFavorite())
			btnFav.setText("Favorisiert");
		else
			btnFav.setText("Favorisieren");
		scrollPane_VideoInfo.setVisible(true);
		lblVideoTitel.setText(video.getName());
		panel_VideoImage.removeAll();
		for (ActionListener listener : btnPlay.getActionListeners())
			btnPlay.removeActionListener(listener);
		btnPlay.addActionListener(e -> video.play());
		for (ActionListener listener : btnFav.getActionListeners())
			btnFav.removeActionListener(listener);
		btnFav.addActionListener(e -> {
			if (video.isFavorite()) {
				video.removeFromFavorites();
				btnFav.setText("Favorisieren");
			} else {
				video.addToFavorites();
				btnFav.setText("Favorisiert");
			}
		});

		textArea_VideoInfo.setText(" \n \n\nLoading...\n \n ");

		// Infos
		new Thread(() -> {
			NormalVideoInfo info = (NormalVideoInfo) video.getVideoInfo();
			lastImg = info.getFrame();
			Utils.openImageFrameVideo(lastImg, panel_VideoImage);
			textArea_VideoInfo.setText(info.getDuration() + "\nErstellt: "
					+ video.createdDate()
					+ "\nGröße: "
					+ video.getFileSizeAsString()
					+ "\nAuflösung: "
					+ info.getWidth()
					+ " x "
					+ info.getHeight()
					+ "\nFormat: "
					+ video.getFormat()
					+ "\nFPS: "
					+ info.getFrameRate());
			File file = new File(lastImg);
			file.delete();
		}).start();
	}

	private JButton getVideoButton(NormalVideo video) {
		JButton button = new JButton(video.getName());
		button.setFont(new Font(font, Font.PLAIN, 11));
		button.setPreferredSize(new Dimension(200, 25));
		button.setOpaque(true);
		button.addActionListener(event -> openVideoInfo(video));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON3) video.getVideoInfo().openImage();
			}
		});
		return button;
	}

	private JButton getFolderButton(Folder folder) {
		JButton button = new JButton(folder.getName());
		button.setToolTipText("<html>" + folder.getName() + "<br></html>");
		button.setFont(new Font(font, Font.PLAIN, 11));
		button.setPreferredSize(new Dimension(200, 25));
		button.setOpaque(true);
		button.addActionListener(event -> {
			currentFolders.add(folder.getName());
			updatePath();
			reinit();
		});
		return button;
	}

	private void reinit() {
		videos.clear();
		folders.clear();
		panel_Videos.removeAll();
		panel_Videos.updateUI();
		panel_Folders.removeAll();
		panel_Folders.updateUI();
		for (File file : new File(path).listFiles()) {
			if (Data.isVideoFile(file.getName())) {
				videos.add(new NormalVideo(file));
				continue;
			} else if (file.getName().contains(".")) continue;
			folders.add(file);
			continue;
		}
		for (NormalVideo video : videos) panel_Videos.add(getVideoButton(video));
		for (File folder : folders) panel_Folders.add(getFolderButton(new Folder(folder)));
	}
}
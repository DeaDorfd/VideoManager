package me.deadorfd.videos.pages;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import me.deadorfd.videos.Main;
import me.deadorfd.videos.utils.Data;
import me.deadorfd.videos.utils.sql.History;
import me.deadorfd.videos.utils.video.HistoryVideo;
import java.awt.Font;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.pages
 * @Date 20.12.2022
 * @Time 03:16:18
 */
public class MainPage extends JPanel {

	/**
	 * Create the panel.
	 */

	public MainPage() {
		setBounds(100, 100, 1280, 720);
		setLayout(null);

		JLabel lblNewLabel = new JLabel("Videos Programm");
		lblNewLabel.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 17));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(454, 0, 363, 23);
		add(lblNewLabel);

		JButton btnFiles = new JButton("Files");
		btnFiles.addActionListener(e -> Main.switchScenes("files"));
		btnFiles.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 13));
		btnFiles.setBounds(454, 135, 363, 42);
		add(btnFiles);

		JButton btnSettingsButton = new JButton("Einstellungen");
		btnSettingsButton.addActionListener(e -> Main.switchScenes("settings"));
		btnSettingsButton.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 13));
		btnSettingsButton.setBounds(454, 188, 363, 42);
		add(btnSettingsButton);

		JButton btnSearch = new JButton("Suchen");
		btnSearch.addActionListener(e -> Main.switchScenes("search"));
		btnSearch.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 13));
		btnSearch.setBounds(454, 347, 363, 42);
		add(btnSearch);

		JButton btnNewVideos = new JButton("Neue Videos");
		btnNewVideos.addActionListener(e -> Main.switchScenes("newfiles"));
		btnNewVideos.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 13));
		btnNewVideos.setBounds(454, 400, 363, 42);
		add(btnNewVideos);

		JButton btnDownloader = new JButton("Download");
		btnDownloader.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 13));
		btnDownloader.setBounds(454, 453, 363, 42);
		btnDownloader.addActionListener(e -> Main.switchScenes("downloader"));
		add(btnDownloader);

		JButton btnHistory = new JButton("Verlauf");
		btnHistory.setFont(new Font("Dialog", Font.PLAIN, 13));
		btnHistory.setBounds(454, 241, 363, 42);
		btnHistory.addActionListener(e -> switchToHistory());
		add(btnHistory);

		JButton btnFavorites = new JButton("Favoriten");
		btnFavorites.setFont(new Font("Dialog", Font.PLAIN, 13));
		btnFavorites.setBounds(454, 294, 363, 42);
		add(btnFavorites);
	}

	private static void switchToHistory() {
		Data.history.clear();
		for (UUID uuid : History.getAllHistory()) Data.history.add(new HistoryVideo(uuid));
		Main.switchScenes("history");
		VideoHistoryPage.updatePanel();
	}
}

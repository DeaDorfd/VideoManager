package me.deadorfd.videos;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;

import javax.swing.JFrame;
import javax.swing.JPanel;
import me.deadorfd.videos.pages.DownloadPage;
import me.deadorfd.videos.pages.FilesPage;
import me.deadorfd.videos.pages.MainPage;
import me.deadorfd.videos.pages.NewFilesPage;
import me.deadorfd.videos.pages.SearchPage;
import me.deadorfd.videos.pages.SettingsPage;
import me.deadorfd.videos.pages.VideoHistoryPage;
import me.deadorfd.videos.utils.FileManager;
import me.deadorfd.videos.utils.sql.History;
import me.deadorfd.videos.utils.sql.SQLite;

import java.awt.CardLayout;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos
 * @Date 18.12.2022
 * @Time 02:31:00
 */
public class Main extends JFrame {

	private static JPanel contentPane;
	private static ServerSocket server;

	/**
	 * Launch the application.
	 */
	// TODO: Add Most Watch List with Counting, add config, add Website page, add
	// history (verlauf) to the buttons and rewrite the watch button, delete button,
	// move to other folder button
	public static void main(String[] args) {
		Instant start = Instant.now();
		FileManager.init();
		SQLite.connect();
		EventQueue.invokeLater(() -> {
			Main frame = new Main();
			frame.setVisible(true);
		});
		History.getAllHistory().forEach(uuid -> {
			History video = new History(uuid);
			if (!new File(video.getPath()).exists()) video.remove();
		});
		Instant end = Instant.now();
		System.out.println("Successfully started. (Took " + Duration.between(start, end).toSeconds() + "s)");
	}

	private static void startServer() {
		try {
			server = new ServerSocket(5000);
			while (true) {
				Socket client = server.accept();
				DataInputStream inputStream = new DataInputStream(client.getInputStream());
				DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
				String info = inputStream.readUTF();
				if (info.equals("getVideoInfo")) {

				} else if (info.equals("getVideos")) {

				} else if (info.equals("getFolders")) {

				} else if (info.equals("getVideo")) {

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ServerSocket getServer() {
		return server;
	}

	/**
	 * Create the frame.
	 */
	private Thread test;

	public Main() {
		setTitle("Videos Programm");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 720);
		contentPane = new JPanel();

		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));

		JPanel main = new MainPage();
		contentPane.add(main, "main");

		JPanel settings = new SettingsPage();
		contentPane.add(settings, "settings");

		JPanel files = new FilesPage();
		contentPane.add(files, "files");

		JPanel search = new SearchPage();
		contentPane.add(search, "search");

		JPanel newFiles = new NewFilesPage();
		contentPane.add(newFiles, "newfiles");

		JPanel historyPage = new VideoHistoryPage();
		contentPane.add(historyPage, "history");

		JPanel downloader = new DownloadPage();
		contentPane.add(downloader, "downloader");

	}

	public static void switchScenes(String name) {
		CardLayout cardLayout = (CardLayout) (contentPane.getLayout());
		cardLayout.show(contentPane, name);
	}
}
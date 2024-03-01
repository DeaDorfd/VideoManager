package me.deadorfd.videos.pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JProgressBar;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.pages
 * @Date 27.03.2023
 * @Time 02:56:29
 */
public class DownloadPage extends JPanel {
	private JTextField linkField;

	/**
	 * Create the panel.
	 */
	public DownloadPage() {
		setBackground(new Color(128, 128, 128));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBounds(100, 100, 848, 774);
		setLayout(null);

		JLabel titel = new JLabel("Videos Programm");
		titel.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 17));
		titel.setHorizontalAlignment(SwingConstants.CENTER);
		titel.setBounds(203, 11, 161, 14);
		add(titel);

		linkField = new JTextField();
		linkField.setBounds(50, 99, 389, 20);
		add(linkField);
		linkField.setColumns(10);

		JButton btnDownload = new JButton("Downloaden");
		btnDownload.addActionListener(e -> {
			maxCount++;
			currentCount++;
			updateProgressbar();
			download(linkField.getText());
			linkField.setText(null);
		});
		btnDownload.setBounds(449, 98, 137, 23);
		add(btnDownload);

		JButton btnInstantDownload = new JButton("Instant Download");
		btnInstantDownload.setBounds(133, 203, 149, 23);
		btnInstantDownload.addActionListener(e -> {
			String clipboard = getSysClipboardText();
			if (clipboard.contains("\n")) {
				for (String s : clipboard.split("\n")) {
					maxCount++;
					currentCount++;
					updateProgressbar();
					download(s);
				}
			} else {
				maxCount++;
				currentCount++;
				updateProgressbar();
				download(clipboard);
			}
		});
		add(btnInstantDownload);

		progressBar = new JProgressBar();
		progressBar.setValue(100);
		progressBar.setBounds(113, 363, 146, 14);
		add(progressBar);

		lblCounts = new JLabel("0 von 0");
		lblCounts.setHorizontalAlignment(SwingConstants.CENTER);
		lblCounts.setBounds(113, 329, 146, 23);
		add(lblCounts);

		JButton btnMainButton = new JButton("Menü");
		btnMainButton.setFont(new Font("", Font.PLAIN, 15));
		btnMainButton.setBounds(364, 354, 120, 23);
		add(btnMainButton);
	}

	/**
	 * get string from Clipboard
	 */
	private String getSysClipboardText() {
		String ret = "";
		Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();

		Transferable clipTf = sysClip.getContents(null);

		if (clipTf != null) {
			if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					ret = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	JProgressBar progressBar;
	int currentCount = 0;
	int maxCount = 0;
	JLabel lblCounts;

	private void updateProgressbar() {
		lblCounts.setText(currentCount + " von " + maxCount);
		int value = currentCount / maxCount * 100;
		progressBar.setValue(value);
	}

	private void download(String url) {
//		new Thread(() -> {
//			YoutubeDLRequest request = new YoutubeDLRequest(url, Data.youtubeDLPPath);
//			request.setOption("paths", Data.downloadPath);
//			request.setOption("format", "bv*[ext=mp4]+ba[ext=m4a]/b[ext=mp4]");
//			request.setOption("output", "%(title)s.%(ext)s");
//			YoutubeDLResponse response = null;
//			try {
//				response = YoutubeDL.execute(request);
//			} catch (YoutubeDLException e2) {
//				e2.printStackTrace();
//			}
//
//			System.out.println(response.getOut());
//			if (response != null && response.getExitCode() == 0) {
//				System.out.println("Das Video wurde erfolgreich heruntergeladen!");
//			} else {
//				System.out.println("Beim Herunterladen des Videos ist ein Fehler aufgetreten.");
//			}
//			currentCount--;
//			updateProgressbar();
//		}).start();
	}
}
package me.deadorfd.videos.utils;

import com.jfoenix.controls.JFXButton;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import me.deadorfd.videos.utils.files.BaseFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils
 * @Date 25.12.2022
 * @Time 00:37:02
 */
public class Utils {

	public static AnchorPane showFileInfo(BaseFile file, EventHandler<? super MouseEvent> value) {
		AnchorPane root = new AnchorPane();
		root.setPrefSize(170, 210);
		root.setStyle("-fx-background-color: #4d5a69;");
		ImageView preview = new ImageView(file.getImage());
		preview.setFitHeight(150.0);
		preview.setFitWidth(170.0);
		preview.setPickOnBounds(true);
		preview.setPreserveRatio(true);
		Label infos = new Label(file.getName() + "\n" + file.getFileInfo().getCreatedDate());
		infos.setFont(new Font("Arial", 12.0));
		infos.setLayoutY(150.0);
		infos.setPrefHeight(60);
		infos.setPrefWidth(170);
		infos.setTextFill(Paint.valueOf("WHITE"));
		infos.setWrapText(true);
		preview.hoverProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) preview.setOpacity(0.7);
			if (!newValue) preview.setOpacity(1);
		});
		preview.setOnMouseClicked(Objects.requireNonNullElseGet(value, () -> event -> {
			file.open();
		}));
		root.getChildren().add(preview);
		root.getChildren().add(infos);
		return root;
	}

	public static void hoverButtonAnimation(JFXButton button) {
		button.hoverProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) button.setOpacity(0.7);
			if (!newValue) button.setOpacity(1);
		});
	}

	public static boolean isCreatedInTheLastDays(File file, int time) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date videodate;
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
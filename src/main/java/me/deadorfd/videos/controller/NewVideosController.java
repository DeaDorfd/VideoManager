package me.deadorfd.videos.controller;

import java.io.File;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import me.deadorfd.videos.App;
import me.deadorfd.videos.utils.Data;
import me.deadorfd.videos.utils.Utils;
import me.deadorfd.videos.utils.video.NormalVideo;
import me.deadorfd.videos.utils.video.info.NormalVideoInfo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 01.03.2024
 * @Time 01:40:22
 */
public class NewVideosController {
	@FXML
	private JFXButton buttonMain;

	@FXML
	private JFXButton buttonOpenFolder;

	@FXML
	private JFXButton buttonBack;

	@FXML
	private ScrollPane videosPane;

	@FXML
	private VBox vboxVideos;

	@FXML
	private JFXCheckBox checkboxTenDays;

	@FXML
	private JFXCheckBox checkboxThirtyDays;

	@FXML
	private void initialize() {
		buttonMain.setOnAction(event -> new App().changePage("Main"));
		videoInfoPane.setVisible(false);
		videosPane.setFitToHeight(true);
		videosPane.setFitToWidth(true);
		vboxVideos.setSpacing(10);
		checkboxTenDays.setOnAction(event -> {
			if (!checkboxTenDays.isSelected()) return;
			if (checkboxThirtyDays.isSelected()) checkboxThirtyDays.setSelected(false);
			vboxVideos.getChildren().clear();
			Platform.runLater(() -> {
				initVideos(10);
				Data.createdInLastThirtyDays
						.forEach(file -> vboxVideos.getChildren().add(getVideoButton(new NormalVideo(file))));
			});
		});
		checkboxThirtyDays.setOnAction(event -> {
			if (!checkboxThirtyDays.isSelected()) return;
			if (checkboxTenDays.isSelected()) checkboxTenDays.setSelected(false);
			vboxVideos.getChildren().clear();
			Platform.runLater(() -> {
				initVideos(30);
				Data.createdInLastThirtyDays
						.forEach(file -> vboxVideos.getChildren().add(getVideoButton(new NormalVideo(file))));
			});
		});
	}

	private void initVideos(int time) {
		if (!Data.createdInLastThirtyDays.isEmpty()) Data.createdInLastThirtyDays.clear();
		getAllNewFiles(Data.oripath, time);
	}

	private void getAllNewFiles(String path, int time) {
		File folder = new File(path);
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				getAllNewFiles(file.getAbsolutePath(), time);
			} else if (Data.isVideoFile(file.getName())) {
				if (Utils.isCreatedInTheLastDays(file, time)) Data.createdInLastThirtyDays.add(file);
			}
		}
	}

	@FXML
	private AnchorPane videoInfoPane;

	@FXML
	private Label videoInfoTitel;

	@FXML
	private ImageView videoInfoImage;

	@FXML
	private JFXTextArea videoInfos;

	@FXML
	private JFXButton videoInfoPlay;

	@FXML
	private JFXButton videoInfoFav;

	private void openVideoInfo(NormalVideo video) {
		if (video.isFavorite())
			videoInfoFav.setText("Favorisiert");
		else
			videoInfoFav.setText("Favorisieren");
		videoInfoPane.setVisible(true);
		videoInfoTitel.setText(video.getName());
		videoInfoImage.setImage(null);
		videoInfoPlay.setOnAction(e -> video.play());
		videoInfoFav.setOnAction(event -> {
			if (video.isFavorite()) {
				video.removeFromFavorites();
				videoInfoFav.setText("Favorisieren");
			} else {
				video.addToFavorites();
				videoInfoFav.setText("Favorisiert");
			}
		});

		videoInfos.setText(" \n \n\nLoading...\n \n ");

		// Infos
		new Thread(() -> {
			NormalVideoInfo info = (NormalVideoInfo) video.getVideoInfo();
			String lastImg = info.getFrame();
			videoInfoImage.setImage(new Image("file:/" + lastImg));
			videoInfos.setText(info.getDuration() + "\nErstellt: "
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

	private JFXButton getVideoButton(NormalVideo video) {
		JFXButton button = new JFXButton(video.getName());
		button.setFont(new Font("Candara", 15));
		button.setPrefSize(350, 35);
		button.setStyle("-fx-background-color: #3D4956; -fx-text-fill: #ffff;");
		button.setOnAction(event -> openVideoInfo(video));
		button.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.MIDDLE) video.getVideoInfo().openImage();
		});
		return button;
	}
}
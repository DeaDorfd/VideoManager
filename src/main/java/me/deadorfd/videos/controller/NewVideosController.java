package me.deadorfd.videos.controller;

import java.io.File;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import me.deadorfd.videos.App;
import me.deadorfd.videos.Video;
import me.deadorfd.videos.utils.Data;
import me.deadorfd.videos.utils.Utils;
import me.deadorfd.videos.utils.video.NormalVideo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 01.03.2024
 * @Time 01:40:22
 */
public class NewVideosController {
	@FXML
	private JFXButton buttonMain, buttonOpenFolder, buttonBack;

	@FXML
	private ScrollPane videosPane;

	@FXML
	private VBox vboxVideos;

	@FXML
	private JFXCheckBox checkboxTenDays, checkboxThirtyDays;

	@FXML
	private AnchorPane root;

	@FXML
	private Label labelTitel;

	private AnchorPane videoInfoPane;

	@FXML
	private void initialize() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/VideoInfoPage.fxml"));
		try {
			videoInfoPane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		VideoInfoController controller = (VideoInfoController) loader.getController();
		videoInfoPane.setLayoutX(478);
		videoInfoPane.setLayoutY(48);
		root.getChildren().add(videoInfoPane);

		buttonMain.setText(Video.getLanguageAPI().getText("generell.button_back"));
		labelTitel.setText(Video.getLanguageAPI().getText("newvideos.label_titel"));
		checkboxTenDays.setText(Video.getLanguageAPI().getText("newvideos.checkbox_tendays"));
		checkboxThirtyDays.setText(Video.getLanguageAPI().getText("newvideos.checkbox_thirtydays"));

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
				Data.createdInLastThirtyDays.forEach(file -> vboxVideos.getChildren()
						.add(getVideoButton(new NormalVideo(file), controller)));
			});
		});
		checkboxThirtyDays.setOnAction(event -> {
			if (!checkboxThirtyDays.isSelected()) return;
			if (checkboxTenDays.isSelected()) checkboxTenDays.setSelected(false);
			vboxVideos.getChildren().clear();
			Platform.runLater(() -> {
				initVideos(30);
				Data.createdInLastThirtyDays.forEach(file -> vboxVideos.getChildren()
						.add(getVideoButton(new NormalVideo(file), controller)));
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

	private JFXButton getVideoButton(NormalVideo video, VideoInfoController controller) {
		JFXButton button = new JFXButton(video.getName());
		button.setFont(new Font("Candara", 15));
		button.setPrefSize(350, 35);
		button.setStyle("-fx-background-color: #3D4956; -fx-text-fill: #ffff;");
		button.setOnAction(event -> {
			videoInfoPane.setVisible(false);
			controller.openVideoInfo(video);
			controller.setOnVideoDelete(video, () -> {
				vboxVideos.getChildren().clear();
				if (checkboxTenDays.isSelected()) initVideos(10);
				if (checkboxThirtyDays.isSelected()) initVideos(30);
				Data.createdInLastThirtyDays.forEach(file -> vboxVideos.getChildren()
						.add(getVideoButton(new NormalVideo(file), controller)));
			});
		});
		button.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.MIDDLE) video.getVideoInfo().openImage();
		});
		return button;
	}
}
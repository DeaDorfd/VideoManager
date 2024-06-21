package me.deadorfd.videos.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import me.deadorfd.videos.App;
import me.deadorfd.videos.Video;
import me.deadorfd.videos.utils.Utils;
import me.deadorfd.videos.utils.files.HistoryFile;
import me.deadorfd.videos.utils.sql.History;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 29.02.2024
 * @Time 00:59:15
 */
public class HistoryController {
	private static final Logger log = LoggerFactory.getLogger(HistoryController.class);
	@FXML
	private JFXButton buttonMain;

	@FXML
	private ScrollPane videosPane;

	@FXML
	private VBox vboxVideos;

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
			log.error("Error: ", e);
		}
		VideoInfoController controller = loader.getController();
		videoInfoPane.setLayoutX(478);
		videoInfoPane.setLayoutY(48);
		root.getChildren().add(videoInfoPane);
		buttonMain.setText(Video.getLanguageAPI().getText("generell.button_back"));
		labelTitel.setText(Video.getLanguageAPI().getText("history.label_titel"));
		buttonMain.setOnAction(event -> new App().changePage("Main"));
		videosPane.setFitToHeight(true);
		videosPane.setFitToWidth(true);
		vboxVideos.setSpacing(10);
		Platform.runLater(() -> {
			for (HistoryFile video : History.getAllHistory())
				vboxVideos.getChildren().add(getVideoButton(video, controller));
		});
	}

	private JFXButton getVideoButton(HistoryFile video, VideoInfoController controller) {
		JFXButton button = new JFXButton(video.getName());
		button.setFont(new Font("Candara", 15));
		button.setPrefSize(200, 25);
		button.setStyle("-fx-background-color: #3D4956; -fx-text-fill: #ffff;");
		Utils.hoverButtonAnimation(button);
		button.setOnAction(event -> {
			videoInfoPane.setVisible(false);
			controller.openVideoInfo(video);
			controller.setOnHistoryDelete(video, () -> {
				vboxVideos.getChildren().clear();
				for (HistoryFile videos : History.getAllHistory())
					vboxVideos.getChildren().add(getVideoButton(videos, controller));
			});
			controller.setOnVideoDelete(video, () -> {
				vboxVideos.getChildren().clear();
				for (HistoryFile videos : History.getAllHistory())
					vboxVideos.getChildren().add(getVideoButton(videos, controller));
			});
		});
		//		button.setOnMouseClicked(event -> {
		//			if (event.getButton() == MouseButton.MIDDLE) video.getVideoInfo().openImage();
		//		});
		return button;
	}
}
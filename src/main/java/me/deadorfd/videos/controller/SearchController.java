package me.deadorfd.videos.controller;

import java.io.File;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

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
import me.deadorfd.videos.utils.video.NormalVideo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 01.03.2024
 * @Time 00:31:14
 */
public class SearchController {
	@FXML
	private JFXButton buttonMain;

	@FXML
	private ScrollPane videosPane;

	@FXML
	private VBox vboxVideos;

	@FXML
	private JFXTextField textfieldSearch;

	@FXML
	private AnchorPane root;

	@FXML
	private Label labelTitel, labelSearch;

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
		labelTitel.setText(Video.getLanguageAPI().getText("search.label_titel"));
		labelSearch.setText(Video.getLanguageAPI().getText("search.label_search"));
		textfieldSearch.setPromptText(Video.getLanguageAPI().getText("search.text_search"));
		buttonMain.setOnAction(event -> new App().changePage("Main"));
		videoInfoPane.setVisible(false);
		videosPane.setFitToHeight(true);
		videosPane.setFitToWidth(true);
		vboxVideos.setSpacing(10);
		textfieldSearch.setOnAction(event -> {
			if (textfieldSearch.getText().isBlank()) return;
			vboxVideos.getChildren().clear();
			Platform.setImplicitExit(false);
			Platform.runLater(() -> {
				onSearch(Data.oripath, controller);
			});
		});
	}

	private void onSearch(String path, VideoInfoController controller) {
		String content = textfieldSearch.getText();
		File folder = new File(path);
		for (File file : folder.listFiles()) {
			String name = file.getName();
			if (file.isDirectory()) {
				onSearch(file.getAbsolutePath(), controller);
			} else if (name.contains(content) || name.equals(content) || name.equalsIgnoreCase(content)
					|| name.startsWith(content)) {
				if (!Data.isVideoFile(name)) continue;
				vboxVideos.getChildren().add(getVideoButton(new NormalVideo(file), controller));
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
				onSearch(Data.oripath, controller);
			});
		});
		button.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.MIDDLE) video.getVideoInfo().openImage();
		});
		return button;
	}
}

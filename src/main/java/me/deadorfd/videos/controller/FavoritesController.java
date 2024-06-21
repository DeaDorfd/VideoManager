package me.deadorfd.videos.controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import me.deadorfd.videos.App;
import me.deadorfd.videos.Video;
import me.deadorfd.videos.utils.files.BaseFile;
import me.deadorfd.videos.utils.sql.Favorites;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 29.02.2024
 * @Time 01:35:15
 */
public class FavoritesController {
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
			e.printStackTrace();
		}
		VideoInfoController controller = (VideoInfoController) loader.getController();
		videoInfoPane.setLayoutX(478);
		videoInfoPane.setLayoutY(48);
		root.getChildren().add(videoInfoPane);
		videosPane.setFitToHeight(true);
		videosPane.setFitToWidth(true);
		vboxVideos.setSpacing(10);
		buttonMain.setText(Video.getLanguageAPI().getText("generell.button_back"));
		labelTitel.setText(Video.getLanguageAPI().getText("favorites.label_titel"));
		buttonMain.setOnAction(event -> new App().changePage("Main"));
		for (BaseFile video : Favorites.getAllFavorites())
			vboxVideos.getChildren().add(getVideoButton(video, controller));
	}

	private JFXButton getVideoButton(BaseFile video, VideoInfoController controller) {
		JFXButton button = new JFXButton(video.getName());
		button.setFont(new Font("Candara", 15));
		button.setPrefSize(200, 25);
		button.setStyle("-fx-background-color: #3D4956; -fx-text-fill: #ffff;");
		button.setOnAction(event -> {
			videoInfoPane.setVisible(false);
			controller.openVideoInfo(video);
			controller.setOnVideoDelete(video, () -> {
				vboxVideos.getChildren().clear();
				for (BaseFile videos : Favorites.getAllFavorites())
					vboxVideos.getChildren().add(getVideoButton(videos, controller));
			});
			controller.setOnVideoFavorite(video, () -> {
				vboxVideos.getChildren().clear();
				for (BaseFile videos : Favorites.getAllFavorites())
					vboxVideos.getChildren().add(getVideoButton(videos, controller));
			});
		});
//		button.setOnMouseClicked(event -> {
//			if (event.getButton() == MouseButton.MIDDLE) video.getVideoInfo().openImage();
//		});
		return button;
	}
}
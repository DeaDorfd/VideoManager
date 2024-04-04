package me.deadorfd.videos.controller;

import java.io.File;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import me.deadorfd.videos.App;
import me.deadorfd.videos.utils.sql.Favorites;
import me.deadorfd.videos.utils.video.BaseVideo;
import me.deadorfd.videos.utils.video.info.NormalVideoInfo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 29.02.2024
 * @Time 01:35:15
 */
public class FavoritesController {
	@FXML
	private JFXButton buttonMain, buttonOpenFolder, buttonBack;

	@FXML
	private ScrollPane videosPane;

	@FXML
	private VBox vboxVideos;

	@FXML
	public void btnOnMainClick(ActionEvent event) {
		new App().changePage("Main");
	}

	@FXML
	private void initialize() {
		videoInfoPane.setVisible(false);
		videosPane.setFitToHeight(true);
		videosPane.setFitToWidth(true);
		vboxVideos.setSpacing(10);
		for (BaseVideo video : Favorites.getAllFavorites())
			vboxVideos.getChildren().add(getVideoButton(video));
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
	private JFXButton videoInfoPlay, videoInfoHistoryDelete, videoInfoOpenVideosTab;

	@FXML
	private FontAwesomeIconView videoInfoFav, videoInfoDelete;

	private void openVideoInfo(BaseVideo video) {
		if (video.isFavorite())
			videoInfoFav.setFill(Paint.valueOf("#ff0000"));
		else
			videoInfoFav.setFill(Paint.valueOf("#000000"));
		videoInfoPane.setVisible(true);
		videoInfoTitel.setText(video.getName());
		videoInfoImage.setImage(null);
		videoInfoPlay.setOnAction(e -> video.play());
		videoInfoOpenVideosTab.setOnAction(event -> video.openInVideosTab());
		videoInfoDelete.setOnMouseClicked(event -> video.delete());
		videoInfoFav.setOnMouseClicked(event -> {
			if (video.isFavorite()) {
				video.removeFromFavorites();
				videoInfoFav.setFill(Paint.valueOf("#000000"));
				vboxVideos.getChildren().clear();
				for (BaseVideo videos : Favorites.getAllFavorites())
					vboxVideos.getChildren().add(getVideoButton(videos));
			} else {
				video.addToFavorites();
				videoInfoFav.setFill(Paint.valueOf("#ff0000"));
				vboxVideos.getChildren().clear();
				for (BaseVideo videos : Favorites.getAllFavorites())
					vboxVideos.getChildren().add(getVideoButton(videos));
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

	private JFXButton getVideoButton(BaseVideo video) {
		JFXButton button = new JFXButton(video.getName());
		button.setFont(new Font("Candara", 15));
		button.setPrefSize(200, 25);
		button.setStyle("-fx-background-color: #3D4956; -fx-text-fill: #ffff;");
		button.setOnAction(event -> openVideoInfo(video));
		button.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.MIDDLE) video.getVideoInfo().openImage();
		});
		return button;
	}
}
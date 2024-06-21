package me.deadorfd.videos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import me.deadorfd.videos.Video;
import me.deadorfd.videos.utils.Utils;
import me.deadorfd.videos.utils.files.BaseFile;
import me.deadorfd.videos.utils.files.HistoryFile;
import me.deadorfd.videos.utils.files.VideoFile;
import me.deadorfd.videos.utils.files.info.VideoInfo;
import me.deadorfd.videos.utils.files.info.VideoLoader;

import java.io.File;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 09.04.2024
 * @Time 01:49:14
 */
public class VideoInfoController {

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
	private FontAwesomeIconView videoInfoFav, videoInfoDelete;

	@FXML
	private JFXButton videoInfoHistoryDelete, videoInfoOpenVideosTab;

	private Boolean isHistoryVideo = false;
	private Boolean isNormalVideo = false;

	@FXML
	private void initialize() {
		videoInfoImage.setImage(null);
		videoInfoPane.setVisible(false);
		videoInfoPane.setNodeOrientation(NodeOrientation.INHERIT);
		videoInfoPlay.setText(Video.getLanguageAPI().getText("videoinfo.button_playvideo"));
		Utils.hoverButtonAnimation(videoInfoPlay);
		videoInfoHistoryDelete.setText(Video.getLanguageAPI().getText("videoinfo.button_historydelete"));
		Utils.hoverButtonAnimation(videoInfoHistoryDelete);
		videoInfoOpenVideosTab.setText(Video.getLanguageAPI().getText("videoinfo.button_openinvideostab"));
		Utils.hoverButtonAnimation(videoInfoOpenVideosTab);
	}

	public void setOnHistoryDelete(HistoryFile video, Runnable run) {
		videoInfoHistoryDelete.setOnAction(event -> {
			video.getHistory().removeAll();
			Platform.runLater(run);
		});
	}

	public void setOnVideoDelete(BaseFile video, Runnable run) {
		videoInfoDelete.setOnMouseClicked(event -> {
			if (!video.delete()) return;
			videoInfoPane.setVisible(false);
			Platform.runLater(run);
		});
	}

	public void setOnVideoFavorite(BaseFile video, Runnable run) {
		videoInfoFav.setOnMouseClicked(event -> {
			if (video.isFavorite()) {
				video.removeFromFavorites();
				videoInfoFav.setFill(Paint.valueOf("#000000"));
				Platform.runLater(run);
			} else {
				video.addToFavorites();
				videoInfoFav.setFill(Paint.valueOf("#ff0000"));
				Platform.runLater(run);
			}
		});
	}

	public void openVideoInfo(BaseFile video) {
		isHistoryVideo = video instanceof HistoryFile;
		isNormalVideo = video instanceof VideoFile;
		videoInfoImage.setImage(null);
		if (video.isFavorite()) videoInfoFav.setFill(Paint.valueOf("#ff0000"));
		else videoInfoFav.setFill(Paint.valueOf("#000000"));
		videoInfoPane.setVisible(true);
		videoInfoTitel.setText(video.getName());
		videoInfoPlay.setOnAction(e -> video.open());
		if (!isHistoryVideo) videoInfoHistoryDelete.setVisible(false);
		if (isNormalVideo) {
			videoInfoOpenVideosTab.setVisible(false);
			videoInfoPlay.setLayoutX(247);
			videoInfoPlay.setLayoutY(516);
		}
		// videoInfoOpenVideosTab.setOnAction(event -> video.openInVideosTab());
		videoInfoFav.setOnMouseClicked(event -> {
			if (video.isFavorite()) {
				video.removeFromFavorites();
				videoInfoFav.setFill(Paint.valueOf("#000000"));
			} else {
				video.addToFavorites();
				videoInfoFav.setFill(Paint.valueOf("#ff0000"));
			}
		});

		videoInfos.setText(" \n \n\nLoading...\n \n ");

		// Infos
		VideoInfo info = (VideoInfo) video.getFileInfo();
		VideoLoader loader = info.getVideoLoader();
		String lastImg = loader.getFrame();
		Image image = new Image("file:/" + lastImg);
		videoInfoImage.setImage(image);
		String normaltext = Video.getLanguageAPI().getText("videoinfo.text_normalvideoinfo").replace("{duration}", loader.getDuration())
				.replace("{created}", info.getCreatedDate()).replace("{size}", info.getFileSizeAsString())
				.replace("{width}", loader.getWidth().toString()).replace("{height}", loader.getHeight().toString())
				.replace("{format}", info.getFormat()).replace("{fps}", loader.getFrameRate());
		String historytext = null;
		if (isHistoryVideo)
			historytext = Video.getLanguageAPI().getText("videoinfo.text_historyvideoinfo").replace("{duration}", loader.getDuration())
					.replace("{created}", info.getCreatedDate()).replace("{size}", info.getFileSizeAsString())
					.replace("{width}", loader.getWidth().toString()).replace("{height}", loader.getHeight().toString())
					.replace("{format}", info.getFormat()).replace("{fps}", loader.getFrameRate())
					.replace("{lastwatched}", ((HistoryFile) video).getHistory().getTimeInString());
		videoInfos.setText((isHistoryVideo ? historytext : normaltext));
		File file = new File(lastImg);
		file.delete();
	}

}

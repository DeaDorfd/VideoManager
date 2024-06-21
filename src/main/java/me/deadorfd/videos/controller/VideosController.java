package me.deadorfd.videos.controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import me.deadorfd.videos.App;
import me.deadorfd.videos.Video;
import me.deadorfd.videos.utils.Data;
import me.deadorfd.videos.utils.Utils;
import me.deadorfd.videos.utils.files.Folder;
import me.deadorfd.videos.utils.files.VideoFile;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

import static me.deadorfd.videos.utils.Data.*;
import static me.deadorfd.videos.utils.FileManager.updatePath;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 06.05.2024
 * @Time 01:05:09
 */
public class VideosController {

	@FXML
	private AnchorPane root;

	@FXML
	private JFXButton buttonMain;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private FlowPane flowPane;

	@FXML
	private FontAwesomeIconView back;

	@FXML
	private ImageView loadingGif;

	@FXML
	private void initialize() {
		buttonMain.setText(Video.getLanguageAPI().getText("generell.button_back"));
		buttonMain.setOnAction(event -> new App().changePage("Main"));
		Utils.hoverButtonAnimation(buttonMain);
		loadingGif.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/loading.gif"))));
		flowPane.setHgap(10);
		flowPane.setVgap(10);
		new Thread(loadStuff()).start();
		back.setOnMouseClicked(event -> {
			if (!currentFolders.isEmpty()) {
				currentFolders.removeLast();
				updatePath();
				reinit();
			}
		});
		//		scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
		//			if (newValue.doubleValue() == scrollPane.getVmax()) {
		//				Platform.runLater(this::loadFiles);
		//			}
		//		});
	}

	private final ArrayList<AnchorPane> files = new ArrayList<>();

	private Task<Void> loadStuff() {
		return new Task<>() {

			@Override
			protected Void call() {
				Instant start = Instant.now();
				files.clear();
				for (Folder folder : Data.folders) {
					files.add(Utils.showFileInfo(folder, e -> {
						currentFolders.add(folder.getName());
						updatePath();
						reinit();
					}));
				}
				for (VideoFile video : Data.videos) {
					files.add(Utils.showFileInfo(video, null));
				}
				Instant end = Instant.now();
				Duration duration = Duration.between(start, end);
				System.out.println("Successfully loaded stuff. (Took " + duration.toSeconds() + "s (" + duration.toMillis() + " ms))");
				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				loadingGif.setVisible(false);
				flowPane.getChildren().addAll(files);
				//for (int i = 0; i < 21; i++) {
				//if (files.size() > i) flowPane.getChildren().add(files.get(i));
				//}
			}
		};
	}

	private Integer currentIndex = 0;

	private void loadFiles() {
		Integer count = 21;
		if (currentIndex + 21 >= files.size()) {
			return;
		}
		int nextIndex = currentIndex + 21;
		for (int i = 0; i < 21; i++) {
			if (!flowPane.getChildren().isEmpty()) {
				flowPane.getChildren().removeFirst();
			}
		}
		for (int i = nextIndex; i < nextIndex + 21; i++) {
			if (i < files.size()) {
				flowPane.getChildren().add(files.get(i));
			}
		}
		scrollPane.setVvalue(0);
		currentIndex = nextIndex;
	}

	//	private AnchorPane showFileInfo(BaseFile file) {
	//		AnchorPane pane = null;
	//		FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/FileInfoPage.fxml"));
	//		try {
	//			pane = loader.load();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//		FileInfoController controller = (FileInfoController) loader.getController();
	//		controller.setInformations(file);
	//		if (file instanceof Folder) controller.setCustomImageClick(e -> {
	//			currentFolders.add(file.getName());
	//			updatePath();
	//			reinit();
	//		});
	//		return pane;
	//	}

	private void reinit() {
		loadingGif.setVisible(true);
		Data.videos.clear();
		folders.clear();
		flowPane.getChildren().clear();
		for (File file : Objects.requireNonNull(new File(path).listFiles())) {
			if (file.isDirectory()) {
				folders.add(new Folder(file));
			} else if (Data.isVideoFile(file.getName())) {
				Data.videos.add(new VideoFile(file));
			}
		}
		new Thread(loadStuff()).start();
	}
}
package me.deadorfd.videos.utils.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;
import me.deadorfd.videos.Video;
import me.deadorfd.videos.utils.files.info.BaseFileInfo;
import me.deadorfd.videos.utils.sql.Favorites;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.files
 * @Date 06.05.2024
 * @Time 01:31:48
 */
public abstract class BaseFile {

	private File file;

	public BaseFile(File file) {
		this.file = file;
	}

	public Boolean delete() {
		Alert alert = new Alert(AlertType.WARNING,
				Video.getLanguageAPI().getText("videoinfo.videodeletealert_text"), ButtonType.YES,
				ButtonType.NO);
		alert.setTitle(Video.getLanguageAPI().getText("videoinfo.videodeletealert_titel"));
		alert.showAndWait();
		ButtonType result = alert.getResult();
		if (result == ButtonType.NO) return false;
		file.delete();
		return true;
	}

	public abstract Image getImage();

	public abstract void open();

	public String getPath() {
		return file.getAbsolutePath().replace('\\', '/');
	}

	public Boolean isFavorite() {
		return new Favorites(this).exists();
	}

	public void addToFavorites() {
		new Favorites(this).create();
	}

	public void removeFromFavorites() {
		new Favorites(this).remove();
	}

	public String getName() {
		return file.getName().replace(".mp4", "").replace(".wmv", "").replace(".ts", "").replace(".mov", "")
				.replace(".MOV", "");
	}

	public abstract BaseFileInfo getFileInfo();

	public File getFile() {
		return file;
	}

	public Boolean exists() {
		return file.exists();
	}

	public byte[] toBase64() {
		try {
			return Base64.getEncoder().encode(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void fromBase64(byte[] base) {
		Instant start = Instant.now();
		byte[] videoBytes = Base64.getDecoder().decode(base);
		try {
			Files.write(Path.of(System.getProperty("java.io.tmpdir") + "/videos/" + file.getName()),
					videoBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Instant end = Instant.now();
		System.out.println("Decoding hat: " + Duration.between(start, end).toSeconds() + "s gebraucht.");
		videoBytes = null;
		start = null;
		end = null;

	}
}
package me.deadorfd.videos.utils.video;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import me.deadorfd.videos.utils.sql.Favorites;
import me.deadorfd.videos.utils.sql.History;
import me.deadorfd.videos.utils.video.info.BaseVideoInfo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils
 * @Date 23.06.2023
 * @Time 01:35:52
 */
public abstract class BaseVideo {

	private File file;

	public BaseVideo(File file) {
		this.file = file;
	}

	public void play() {
		try {
			Desktop.getDesktop().open(file);
			History.create(getPath());
		} catch (IOException e) {}
	}

	public String getPath() {
		return file.getAbsolutePath();
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
		return file.getName().replaceAll(".mp4", "").replaceAll(".wmv", "").replaceAll(".ts", "")
				.replaceAll(".mov", "").replaceAll(".MOV", "");
	}

	public String getFormat() {
		if (file.getName().contains(".mp4")) return "mp4";
		if (file.getName().contains(".wmv")) return "wmv";
		if (file.getName().contains(".ts")) return "ts";
		if (file.getName().contains(".mov") || file.getName().contains(".MOV")) return "mov";
		return "Error";
	}

	public String createdDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Date videodate = null;
		try {
			videodate = sdf.parse(sdf.format(file.lastModified()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(file.lastModified());
	}

	public abstract BaseVideoInfo getVideoInfo();

	public File getFile() {
		return file;
	}

	public Boolean exists() {
		return file.exists();
	}

	public long getFileSize() {
		return file.length();
	}

	public String getFileSizeAsString() {
		long fileSize = file.length();

		DecimalFormat decimalFormat = new DecimalFormat("#.##");

		String formattedSize;
		if (fileSize < 1024) {
			formattedSize = fileSize + " bytes";
		} else if (fileSize < 1024 * 1024) {
			Double kilobytes = (double) fileSize / 1024;
			Long kb = kilobytes.longValue();
			if (kb.toString().length() >= 2) {
				formattedSize = kb + " KB";
			} else
				formattedSize = decimalFormat.format(kilobytes) + " KB";
		} else if (fileSize < 1024 * 1024 * 1024) {
			Double megabytes = (double) fileSize / (1024 * 1024);
			Long mb = megabytes.longValue();
			if (mb.toString().length() >= 2) {
				formattedSize = mb + " MB";
			} else
				formattedSize = decimalFormat.format(megabytes) + " MB";
		} else {
			Double gigabytes = (double) fileSize / (1024 * 1024 * 1024);
			Long gb = gigabytes.longValue();
			if (gb.toString().length() >= 2) {
				formattedSize = gb + " GB";
			} else
				formattedSize = decimalFormat.format(gigabytes) + " GB";
		}
		return formattedSize;
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

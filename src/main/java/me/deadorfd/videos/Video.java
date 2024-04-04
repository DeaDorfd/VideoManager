package me.deadorfd.videos;

import java.time.Duration;
import java.time.Instant;

import me.deadorfd.videos.utils.Data;
import me.deadorfd.videos.utils.FileManager;
import me.deadorfd.videos.utils.Settings;
import me.deadorfd.videos.utils.Utils;
import me.deadorfd.videos.utils.sql.Favorites;
import me.deadorfd.videos.utils.sql.SQLite;
import me.deadorfd.videos.utils.sql.Setting;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos
 * @Date 29.01.2024
 * @Time 01:30:13
 */
public class Video {

	// #3D4956 : dark , #4d5a69 : light

	public static void main(String[] args) {
		Instant start = Instant.now();
		SQLite.connect();
		Data.path = new Setting(Settings.DEFAULT_PATH).getStatus();
		Data.oripath = new Setting(Settings.DEFAULT_PATH).getStatus();
		FileManager.init();
		System.out.println(50 / 100.0);
		System.out.println(0.5 * 100);
		Favorites.getAllFavorites().forEach(video -> {
			if (!video.getFile().exists()) {
				String newPath = Utils.getPathIfFileDoesntExists(video.getFile(), Data.oripath);
				if (newPath != null) {
					new Favorites(video).setVideoPath(newPath);
				} else
					video.removeFromFavorites();
			}
		});
		Instant end = Instant.now();
		System.out.println("Successfully started. (Took " + Duration.between(start, end).toSeconds() + "s)");
		App.main(args);
	}
}
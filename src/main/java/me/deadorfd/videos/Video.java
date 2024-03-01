package me.deadorfd.videos;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

import me.deadorfd.videos.utils.Data;
import me.deadorfd.videos.utils.FileManager;
import me.deadorfd.videos.utils.Settings;
import me.deadorfd.videos.utils.sql.Favorites;
import me.deadorfd.videos.utils.sql.History;
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
		Data.path = new Setting(Settings.DEFAULT_PATH).getStatus() + "/";
		Data.oripath = new Setting(Settings.DEFAULT_PATH).getStatus() + "/";
		FileManager.init();
		History.getAllHistory().forEach(uuid -> {
			History video = new History(uuid);
			if (!new File(video.getPath()).exists()) video.remove();
		});
		Favorites.getAllFavorites().forEach(video -> {
			if (!video.getFile().exists()) video.removeFromFavorites();
		});
		Instant end = Instant.now();
		System.out.println("Successfully started. (Took " + Duration.between(start, end).toSeconds() + "s)");
		App.main(args);
	}
}
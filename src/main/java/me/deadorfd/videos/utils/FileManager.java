package me.deadorfd.videos.utils;

import java.io.File;

import me.deadorfd.videos.utils.video.NormalVideo;
import static me.deadorfd.videos.utils.Data.*;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils
 * @Date 18.12.2022
 * @Time 04:18:49
 */
public class FileManager {
	public static void init() {
		for (File file : new File(path).listFiles()) {
			if (Data.isVideoFile(file.getName())) {
				videos.add(new NormalVideo(file));
				continue;
			} else if (file.isDirectory()) {
				folders.add(file);
				continue;
			}
		}
	}

	public static void updatePath() {
		path = oripath;
		for (String folder : currentFolders) path = path + "/" + folder;
	}
}
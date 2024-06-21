package me.deadorfd.videos.utils;

import java.io.File;

import me.deadorfd.videos.utils.files.Folder;
import me.deadorfd.videos.utils.files.VideoFile;
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
		videos.clear();
		folders.clear();
		for (File file : new File(path).listFiles()) {
			if (file.isDirectory()) {
				folders.add(new Folder(file));
				continue;
			} else if (Data.isVideoFile(file.getName())) {
				videos.add(new VideoFile(file));
				continue;
			}
		}
	}

	public static void updatePath() {
		path = oripath;
		for (String folder : currentFolders) path = path + "/" + folder;
	}
}
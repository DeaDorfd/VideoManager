package me.deadorfd.videos.utils;

import java.io.File;
import java.util.ArrayList;

import me.deadorfd.videos.utils.files.Folder;
import me.deadorfd.videos.utils.files.VideoFile;
import me.deadorfd.videos.utils.sql.Setting;
import me.deadorfd.videos.utils.video.HistoryVideo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils
 * @Date 19.12.2022
 * @Time 02:24:07
 */
public class Data {

	public static ArrayList<File> createdInLastThirtyDays = new ArrayList<>();

	// Videos
	public static ArrayList<VideoFile> videos = new ArrayList<>();
	public static ArrayList<HistoryVideo> history = new ArrayList<>();

	// Folders
	public static ArrayList<Folder> folders = new ArrayList<>();
	public static ArrayList<String> currentFolders = new ArrayList<>();

	public static String path = new Setting(Settings.DEFAULT_PATH).getStatus();
	public static String oripath = new Setting(Settings.DEFAULT_PATH).getStatus();

	public static String downloadPath = "";

	public static String youtubeDLPPath = "";

	public static Thread currentThread;

	public static boolean isVideoFile(String name) {
		return name.contains(".wmv") || name.contains(".mp4") || name.contains(".ts") || name.contains(".mov")
				|| name.contains(".MOV") || name.contains(".MP4");
	}
}

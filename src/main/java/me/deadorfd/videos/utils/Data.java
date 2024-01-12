package me.deadorfd.videos.utils;

import java.io.File;
import java.util.ArrayList;

import me.deadorfd.videos.utils.video.HistoryVideo;
import me.deadorfd.videos.utils.video.NormalVideo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils
 * @Date 19.12.2022
 * @Time 02:24:07
 */
public class Data {

	public static String font = "Segoe Print";

	public static ArrayList<File> createdInLastThirtyDays = new ArrayList<>();

	// Videos
	public static ArrayList<NormalVideo> videos = new ArrayList<>();
	public static ArrayList<HistoryVideo> history = new ArrayList<>();

	// Folders
	public static ArrayList<File> folders = new ArrayList<>();
	public static ArrayList<String> currentFolders = new ArrayList<>();

	public static String path = "E:/Videos/";
	public static String oripath = "E:/Videos/";

	public static String downloadPath = "";

	public static String youtubeDLPPath = "";

	public static Thread currentThread;

	public static boolean isVideoFile(String name) {
		return name.contains(".wmv") || name.contains(".mp4") || name.contains(".ts") || name.contains(".mov")
				|| name.contains(".MOV");
	}
}

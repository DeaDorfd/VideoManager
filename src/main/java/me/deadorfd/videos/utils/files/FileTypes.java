package me.deadorfd.videos.utils.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.files
 * @Date 06.05.2024
 * @Time 03:18:51
 */
public enum FileTypes {

	VIDEO(".MP4", ".mp4", ".wmv", ".ts", ".MOV", ".mov"), FOLDER(), IMAGE(".PNG", ".png", ".jpg"),
	TEXT(".txt", ".docs"), ARCHIVE(".rar", ".zip");

	private ArrayList<String> types;

	private FileTypes(String... types) {
		this.types = new ArrayList<>();
		this.types.addAll(Arrays.asList(types));
	}

	public ArrayList<String> getTypes() {
		return types;
	}

	public Boolean contains(File file) {
		for (String type : getTypes()) {
			if (file.getName().contains(type)) return true;
		}
		return false;
	}

	public static FileTypes getFileTypes(File file) {
		if (file.isDirectory()) return FOLDER;
		for (FileTypes types : values()) {
			if (types.getTypes().isEmpty()) continue;
			if (types.contains(file)) return types;
		}
		return null;
	}

	public static String getVideoType(File file) {
		if (file.getName().contains(".mp4") || file.getName().contains(".MP4")) return "mp4";
		if (file.getName().contains(".wmv")) return "wmv";
		if (file.getName().contains(".ts")) return "ts";
		if (file.getName().contains(".mov") || file.getName().contains(".MOV")) return "mov";
		return null;
	}

}

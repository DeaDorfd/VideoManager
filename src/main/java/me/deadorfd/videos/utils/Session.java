package me.deadorfd.videos.utils;

import static me.deadorfd.videos.utils.Data.oripath;

import java.util.ArrayList;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils
 * @Date 03.03.2024
 * @Time 02:21:09
 */
public class Session {

	private String path;
	private String videoInfoPath;
	private Double videoVValue;
	private Double folderVValue;

	public Session(String path) {
		this.path = path;
		this.videoInfoPath = "";
		this.videoVValue = 0.0;
		this.folderVValue = 0.0;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setVideoInfoPath(String videoInfoPath) {
		this.videoInfoPath = videoInfoPath;
	}

	public String getVideoInfoPath() {
		return videoInfoPath;
	}

	public void setVideoVValue(Double videoVValue) {
		this.videoVValue = videoVValue;
	}

	public Double getVideoVValue() {
		return videoVValue;
	}

	public void setFolderVValue(Double folderVValue) {
		this.folderVValue = folderVValue;
	}

	public Double getFolderVValue() {
		return folderVValue;
	}

	public ArrayList<String> getFolders() {
		ArrayList<String> folders = new ArrayList<>();
		String path = this.path;
		path = path.replaceAll(oripath, "");
		for (String folder : path.split("/"))
			if (!Data.isVideoFile(folder) & !folder.isBlank()) folders.add(folder);
		return folders;
	}

}
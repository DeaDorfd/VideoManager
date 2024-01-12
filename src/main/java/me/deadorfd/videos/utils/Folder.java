package me.deadorfd.videos.utils;

import java.io.File;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils
 * @Date 23.06.2023
 * @Time 02:48:08
 */
public class Folder {

	private File folder;

	public Folder(File folder) {
		this.folder = folder;
	}

	public String getName() {
		return folder.getName();
	}

	public String getPath() {
		return folder.getAbsolutePath();
	}

}

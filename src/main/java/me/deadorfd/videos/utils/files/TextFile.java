package me.deadorfd.videos.utils.files;

import java.io.File;

import javafx.scene.image.Image;
import me.deadorfd.videos.utils.files.info.BaseFileInfo;
import me.deadorfd.videos.utils.files.info.FileInfo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.files
 * @Date 06.05.2024
 * @Time 03:13:48
 */
public class TextFile extends BaseFile {

	public TextFile(File file) {
		super(file);
	}

	@Override
	public void open() {

	}

	@Override
	public BaseFileInfo getFileInfo() {
		return new FileInfo(this);
	}

	@Override
	public Image getImage() {
		return null;
	}

}

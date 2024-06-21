package me.deadorfd.videos.utils.files;

import javafx.scene.image.Image;
import me.deadorfd.videos.utils.files.info.BaseFileInfo;
import me.deadorfd.videos.utils.files.info.FileInfo;

import java.io.File;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.files
 * @Date 06.05.2024
 * @Time 03:15:18
 */
public class ImageFile extends BaseFile {

	public ImageFile(File file) {
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
		return new Image("file:/" + getPath(), true);
	}

}

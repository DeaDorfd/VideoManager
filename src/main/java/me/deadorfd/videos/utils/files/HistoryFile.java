package me.deadorfd.videos.utils.files;

import java.io.File;
import java.util.UUID;

import javafx.scene.image.Image;
import me.deadorfd.videos.utils.files.info.BaseFileInfo;
import me.deadorfd.videos.utils.files.info.FileInfo;
import me.deadorfd.videos.utils.files.info.VideoInfo;
import me.deadorfd.videos.utils.sql.History;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.files
 * @Date 06.05.2024
 * @Time 02:52:57
 */
public class HistoryFile extends BaseFile {

	private UUID uuid;

	public HistoryFile(UUID uuid) {
		super(new File(new History(uuid).getPath()));
		this.uuid = uuid;
	}

	@Override
	public BaseFileInfo getFileInfo() {
		FileTypes type = FileTypes.getFileTypes(getFile());
		if (type == FileTypes.VIDEO) return new VideoInfo(this);
		return new FileInfo(this);
	}

	@Override
	public void open() {
		getType().open();
	}

	public UUID getUUID() {
		return uuid;
	}

	public History getHistory() {
		return new History(uuid);
	}

	public BaseFile getType() {
		FileTypes type = FileTypes.getFileTypes(getFile());
		if (type == FileTypes.FOLDER) return new Folder(getFile());
		if (type == FileTypes.VIDEO) return new VideoFile(getFile());
		if (type == FileTypes.ARCHIVE) return new ArchiveFile(getFile());
		if (type == FileTypes.IMAGE) return new ImageFile(getFile());
		if (type == FileTypes.TEXT) return new TextFile(getFile());
		return null;
	}

	@Override
	public Image getImage() {
		return null;
	}

}

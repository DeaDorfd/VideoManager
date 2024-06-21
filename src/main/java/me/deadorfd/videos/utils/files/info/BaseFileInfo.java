package me.deadorfd.videos.utils.files.info;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import me.deadorfd.videos.utils.files.BaseFile;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.files.info
 * @Date 06.05.2024
 * @Time 01:31:34
 */
public abstract class BaseFileInfo {

	private BaseFile baseFile;

	public BaseFileInfo(BaseFile baseFile) {
		this.baseFile = baseFile;
	}

	public long getFileSize() {
		return baseFile.getFile().length();
	}

	public String getFileSizeAsString() {
		long fileSize = baseFile.getFile().length();

		DecimalFormat decimalFormat = new DecimalFormat("#.##");

		String formattedSize;
		if (fileSize < 1024) {
			formattedSize = fileSize + " bytes";
		} else if (fileSize < 1024 * 1024) {
			Double kilobytes = (double) fileSize / 1024;
			Long kb = kilobytes.longValue();
			if (kb.toString().length() >= 2) {
				formattedSize = kb + " KB";
			} else
				formattedSize = decimalFormat.format(kilobytes) + " KB";
		} else if (fileSize < 1024 * 1024 * 1024) {
			Double megabytes = (double) fileSize / (1024 * 1024);
			Long mb = megabytes.longValue();
			if (mb.toString().length() >= 2) {
				formattedSize = mb + " MB";
			} else
				formattedSize = decimalFormat.format(megabytes) + " MB";
		} else {
			Double gigabytes = (double) fileSize / (1024 * 1024 * 1024);
			Long gb = gigabytes.longValue();
			if (gb.toString().length() >= 2) {
				formattedSize = gb + " GB";
			} else
				formattedSize = decimalFormat.format(gigabytes) + " GB";
		}
		return formattedSize;
	}

	public String getFormat() {
		if (baseFile.getFile().getName().contains(".mp4")) return "mp4";
		if (baseFile.getFile().getName().contains(".wmv")) return "wmv";
		if (baseFile.getFile().getName().contains(".ts")) return "ts";
		if (baseFile.getFile().getName().contains(".mov") || baseFile.getFile().getName().contains(".MOV"))
			return "mov";
		return "Error";
	}

	public String getCreatedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		return sdf.format(getBaseFile().getFile().lastModified());
	}

	public BaseFile getBaseFile() {
		return baseFile;
	}

}

package me.deadorfd.videos.utils.files.info;

import java.util.ArrayList;
import me.deadorfd.videos.utils.files.BaseFile;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.files.info
 * @Date 06.05.2024
 * @Time 03:01:55
 */
public class VideoInfo extends BaseFileInfo {

	public static ArrayList<VideoInfo> infos = new ArrayList<>();

	private Integer getInfosInt(BaseFileInfo info) {
		Integer i = 0;
		for (BaseFileInfo vInfos : infos) {
			if (vInfos.getBaseFile() == info.getBaseFile()) break;
			i++;
		}
		return i;
	}

	public VideoInfo(BaseFile baseFile) {
		super(baseFile);
	}

	public VideoLoader getVideoLoader() {
		return new VideoLoader(this);
	}

}
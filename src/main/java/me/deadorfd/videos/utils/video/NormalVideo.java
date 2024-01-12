package me.deadorfd.videos.utils.video;

import java.io.File;

import me.deadorfd.videos.utils.video.info.BaseVideoInfo;
import me.deadorfd.videos.utils.video.info.NormalVideoInfo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.video
 * @Date 08.12.2023
 * @Time 01:56:25
 */
public class NormalVideo extends BaseVideo {

	public NormalVideo(File file) {
		super(file);
	}

	@Override
	public BaseVideoInfo getVideoInfo() {
		return new NormalVideoInfo(this);
	}
}
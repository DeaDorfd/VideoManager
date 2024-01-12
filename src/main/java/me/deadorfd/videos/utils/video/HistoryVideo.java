package me.deadorfd.videos.utils.video;

import java.io.File;
import java.util.UUID;

import me.deadorfd.videos.utils.sql.History;
import me.deadorfd.videos.utils.video.info.BaseVideoInfo;
import me.deadorfd.videos.utils.video.info.NormalVideoInfo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.video
 * @Date 08.12.2023
 * @Time 02:01:04
 */
public class HistoryVideo extends BaseVideo {

	private UUID uuid;

	public HistoryVideo(UUID uuid) {
		super(new File(new History(uuid).getPath()));
		this.uuid = uuid;
	}

	@Override
	public BaseVideoInfo getVideoInfo() {
		return new NormalVideoInfo(this);
	}

	public UUID getUUID() {
		return uuid;
	}

	public History getHistory() {
		return new History(uuid);
	}

}

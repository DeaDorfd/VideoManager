package me.deadorfd.videos.utils;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils
 * @Date 29.02.2024
 * @Time 03:53:11
 */
public enum Settings {

	DEBUG_MODE("debugMode"), DEFAULT_PATH("defaultPath"), HISTORY_CLEAR("historyclear"),
	HISTORY_CLEAR_DAYS("historycleardays"), MEDIAPLAYER_VOLUME("mediaplayervolume");

	private String name;

	private Settings(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
package me.deadorfd.videos.utils.language;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.language
 * @Date 16.04.2024
 * @Time 02:47:06
 */
public enum Languages {

	GERMAN("German", "messages_DE"), ENGLISH("English", "messages_EN");

	private String name;
	private String messageFileName;

	private Languages(String name, String messageFileName) {
		this.name = name;
		this.messageFileName = messageFileName;
	}

	public String getName() {
		return name;
	}

	public String getMessageFileName() {
		return messageFileName;
	}

	public static Languages getByName(String name) {
		for (Languages language : values()) if (language.getName().equals(name)) return language;
		return Languages.ENGLISH;
	}

}

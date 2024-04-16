package me.deadorfd.videos.utils.language;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.deadorfd.videos.utils.Settings;
import me.deadorfd.videos.utils.sql.Setting;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.language
 * @Date 16.04.2024
 * @Time 02:46:56
 */
public class LanguageAPI {

	private String getJSONFromFile(Languages language) {
		String jsonText = "";

		InputStream file = getClass().getResourceAsStream("/lang/" + language.getMessageFileName() + ".json");
		if (file == null) return jsonText;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(file));
			String line;
			while ((line = reader.readLine()) != null)
				jsonText += line + "\n";
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonText;
	}

	private HashMap<Languages, String> languageMessages = new HashMap<Languages, String>();

	public LanguageAPI() {
		for (Languages lang : Languages.values()) languageMessages.put(lang, getJSONFromFile(lang));
	}

	public String getText(String message) {
		JSONParser parser = new JSONParser();
		String text = null;
		try {
			Object object = parser.parse(
					languageMessages.get(Languages.getByName(new Setting(Settings.LANGUAGE).getStatus())));
			JSONObject jsonObject = (JSONObject) object;
			text = (String) jsonObject.get(message);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (text == null) text = message;
		return text;
	}

}

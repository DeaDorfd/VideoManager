package me.deadorfd.videos.utils.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.deadorfd.videos.utils.Settings;
import me.deadorfd.videos.utils.language.Languages;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.sql
 * @Date 29.02.2024
 * @Time 03:30:48
 */
public class Setting {

	private Settings setting;

	public Setting(Settings setting) {
		this.setting = setting;
	}

	public void create(String status) {
		if (!exists()) SQLite.update(
				"INSERT INTO Settings(Name, Status) VALUES ('" + setting.getName() + "','" + status + "');");
	}

	public Boolean exists() {
		if (!SQLite.isConnected()) return false;
		try {
			ResultSet rs = SQLite.getResult("SELECT * FROM Settings WHERE Name= '" + setting.getName() + "'");
			if (rs.next()) return rs.getString("Name") != null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getStatus() {
		if (!exists()) {
			if (setting == Settings.HISTORY_CLEAR_DAYS) return "90";
			if (setting == Settings.MEDIAPLAYER_VOLUME) return "1.0";
			if (setting == Settings.LANGUAGE) return Languages.ENGLISH.getName();
			return "";
		}
		ResultSet rs = SQLite.getResult("Select Status FROM Settings WHERE Name='" + setting.getName() + "'");
		try {
			while (rs.next())
				return rs.getString("Status");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void setStatus(String status) {
		if (!exists()) {
			create(status);
			return;
		}
		SQLite.update("UPDATE Settings SET Status= '" + status + "' WHERE Name= '" + setting.getName() + "'");
	}

}

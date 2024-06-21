package me.deadorfd.videos.utils.sql;

import me.deadorfd.videos.utils.Data;
import me.deadorfd.videos.utils.Settings;
import me.deadorfd.videos.utils.Utils;
import me.deadorfd.videos.utils.files.HistoryFile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.sql
 * @Date 21.11.2023
 * @Time 02:58:34
 */
public class History {

	private final UUID uuid;

	public History(UUID uuid) {
		this.uuid = uuid;
	}

	public static void create(String path) {
		if (isInSameDay(path)) {
			for (UUID oldUUID : getAllInSameDay(path)) new History(oldUUID).remove();
		}
		UUID uuid = UUID.randomUUID();
		try {
			PreparedStatement ps = SQLite.conn.prepareStatement("INSERT INTO History(UUID, VideoPath, WatchTime) VALUES (?,?,?)");
			ps.setString(1, uuid.toString());
			ps.setString(2, path);
			ps.setLong(3, Instant.now().getEpochSecond());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Boolean exists() {
		if (!SQLite.isConnected()) return false;
		try {
			PreparedStatement ps = SQLite.conn.prepareStatement("SELECT * FROM History WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			boolean exists = rs.next();
			rs.close();
			ps.close();
			return exists;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void setPath(String path) {
		if (!exists()) return;
		try {
			PreparedStatement ps = SQLite.conn.prepareStatement("UPDATE History SET VideoPath=? WHERE UUID=?");
			ps.setString(1, path);
			ps.setString(2, uuid.toString());
			int i = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getPath() {
		if (!exists()) return "";
		ResultSet rs = SQLite.getResult("Select VideoPath FROM History WHERE UUID='" + uuid + "'");
		try {
			if (rs.next()) return rs.getString("VideoPath");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public Long getTime() {
		if (!exists()) return 0L;
		ResultSet rs = SQLite.getResult("Select WatchTime FROM History WHERE UUID='" + uuid + "'");
		try {
			if (rs.next()) return rs.getLong("WatchTime");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}

	public String getTimeInString() {
		if (!exists()) return "";
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMANY);
		ResultSet rs = SQLite.getResult("Select WatchTime FROM History WHERE UUID='" + uuid + "'");
		Date date = new Date();
		try {
			if (rs.next()) date = Date.from(Instant.ofEpochSecond(rs.getLong("WatchTime")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return format.format(date);
	}

	public Date getDate() {
		if (!exists()) return null;
		ResultSet rs = SQLite.getResult("Select WatchTime FROM History WHERE UUID='" + uuid + "'");
		try {
			if (rs.next()) return Date.from(Instant.ofEpochSecond(rs.getLong("WatchTime")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isWatchedInTheLastDays(Integer time) {
		if (!exists()) return false;
		Calendar daysAgo = Calendar.getInstance();
		daysAgo.add(Calendar.DAY_OF_MONTH, -time);
		Date daysAgoDate = daysAgo.getTime();
		return getDate().before(daysAgoDate);
	}

	public Boolean remove() {
		if (!SQLite.isConnected()) return false;
		try {
			PreparedStatement ps = SQLite.conn.prepareStatement("DELETE FROM History WHERE UUID='" + uuid + "'");
			int i = ps.executeUpdate();
			ps.close();
			return i == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Boolean removeAll() {
		if (!SQLite.isConnected()) return false;
		try {
			PreparedStatement ps = SQLite.conn.prepareStatement("DELETE FROM History WHERE VideoPath=?");
			ps.setString(1, getPath());
			int i = ps.executeUpdate();
			ps.close();
			return i == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<HistoryFile> getAllHistory() {
		ArrayList<HistoryFile> history = new ArrayList<>();
		try {
			PreparedStatement ps = SQLite.conn.prepareStatement("SELECT * FROM History ORDER BY ID DESC");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				HistoryFile video = new HistoryFile(UUID.fromString(rs.getString("UUID")));
				if (Boolean.parseBoolean(new Setting(Settings.HISTORY_CLEAR).getStatus())) {
					if (video.getHistory().isWatchedInTheLastDays(Integer.valueOf(new Setting(Settings.HISTORY_CLEAR_DAYS).getStatus()))) {
						video.getHistory().remove();
						continue;
					}
				}
				if (!video.getFile().exists()) {
					String newPath = Utils.getPathIfFileDoesntExists(video.getFile(), Data.oripath);
					if (newPath != null) {
						video.getHistory().setPath(newPath);
						history.add(new HistoryFile(UUID.fromString(rs.getString("UUID"))));
					} else {
						video.getHistory().remove();
					}
				} else {
					history.add(video);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return history;
	}

	public static HashMap<Integer, UUID> getTop10History() {
		HashMap<Integer, UUID> rang = new HashMap<Integer, UUID>();
		try {
			ResultSet rs = SQLite.getResult("SELECT * FROM History DESC LIMIT 10");
			int i = 0;
			while (rs.next()) {
				i++;
				rang.put(i, UUID.fromString(rs.getString("UUID")));
			}
			rs.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return rang;
	}

	public static Boolean isInSameDay(String path) {
		LocalDate lDateNow = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate();
		for (HistoryFile video : getAllHistory()) {
			History history = video.getHistory();
			if (!history.getPath().equals(path)) continue;
			LocalDate lDateVideo = Instant.ofEpochSecond(history.getTime()).atOffset(ZoneOffset.UTC).toLocalDate();
			if (lDateNow.getDayOfMonth() == lDateVideo.getDayOfMonth() && lDateNow.getDayOfWeek() == lDateVideo.getDayOfWeek()
					&& lDateNow.getYear() == lDateVideo.getYear()) return true;
		}
		return false;
	}

	public static ArrayList<UUID> getAllInSameDay(String path) {
		ArrayList<UUID> uuidList = new ArrayList<>();
		LocalDate lDateNow = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate();
		for (HistoryFile video : getAllHistory()) {
			History history = video.getHistory();
			if (!history.getPath().equals(path)) continue;
			LocalDate lDateVideo = Instant.ofEpochSecond(history.getTime()).atOffset(ZoneOffset.UTC).toLocalDate();
			if (lDateNow.getDayOfMonth() == lDateVideo.getDayOfMonth() && lDateNow.getDayOfWeek() == lDateVideo.getDayOfWeek()
					&& lDateNow.getYear() == lDateVideo.getYear()) uuidList.add(video.getUUID());
		}
		return uuidList;
	}

}

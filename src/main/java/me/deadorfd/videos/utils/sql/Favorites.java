package me.deadorfd.videos.utils.sql;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import me.deadorfd.videos.utils.video.BaseVideo;
import me.deadorfd.videos.utils.video.NormalVideo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.sql
 * @Date 20.11.2023
 * @Time 01:18:34
 */
public class Favorites {

	private BaseVideo video;

	public Favorites(BaseVideo video) {
		this.video = video;
	}

	public void create() {
		if (exists()) return;
		try {
			PreparedStatement ps = SQLite.conn
					.prepareStatement("INSERT INTO Favorites(VideoPath) VALUES (?)");
			ps.setString(1, video.getPath());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Boolean exists() {
		if (!SQLite.isConnected()) return false;

		try {
			PreparedStatement ps = SQLite.conn.prepareStatement("SELECT * FROM Favorites WHERE VideoPath=?");
			ps.setString(1, video.getPath());
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

	public Boolean remove() {
		if (!SQLite.isConnected()) return false;
		try {
			PreparedStatement ps = SQLite.conn.prepareStatement("DELETE FROM Favorites WHERE VideoPath=?");
			ps.setString(1, video.getPath());
			int i = ps.executeUpdate();
			ps.close();
			return i == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void setVideoPath(String path) {
		if (!exists()) return;
		try {
			PreparedStatement ps = SQLite.conn
					.prepareStatement("UPDATE Favorites SET VideoPath=? WHERE VideoPath=?");
			ps.setString(1, path);
			ps.setString(2, video.getPath());
			int i = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<BaseVideo> getAllFavorites() {
		ArrayList<BaseVideo> favorites = new ArrayList<>();
		try {
			PreparedStatement ps = SQLite.conn.prepareStatement("SELECT * FROM Favorites ORDER BY ID DESC");
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				favorites.add(new NormalVideo(new File(rs.getString("VideoPath"))));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return favorites;
	}

}

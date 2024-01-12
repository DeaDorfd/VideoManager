package me.deadorfd.videos.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.manager
 * @Date 20.12.2022
 * @Time 03:15:17
 */
public class ConfigManager {

	private static String path = System.getenv("APPDATA") + "/.videos/config.yml";

	private static String createConfigLineArray(String... config) {
		String arg = "";
		List<String> lines = Arrays.asList(config);
		for (String conf : lines) {
			if (!arg.isEmpty()) {
				arg = arg + "\n" + conf;
				continue;
			}
			arg = conf;
			continue;
		}
		return arg;
	}

	private static String createConfigLineArray(List<String> lines) {
		String arg = "";
		for (String conf : lines) {
			if (!arg.isEmpty()) {
				arg = arg + "\n" + conf;
				continue;
			}
			arg = conf;
			continue;
		}
		return arg;
	}

	public static boolean createDefaultConfig() {
		File folder = new File(System.getenv("APPDATA") + "/.videos");
		if (!folder.exists()) folder.mkdir();
		File file = new File(path);
		if (file.exists()) return true;
		if (!file.exists()) try {
			file.createNewFile();
		} catch (IOException e) {}
		String config = createConfigLineArray("debugmode=false", "test=false");
		try {
			FileWriter writer;
			writer = new FileWriter(file);
			writer.write(config);
			writer.close();
		} catch (IOException e) {}
		return true;
	}

	private static String basic(String arg) {
		arg = arg.replaceAll("=", "");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(path)));
		} catch (FileNotFoundException e1) {}
		String st;
		String config = "default=false";
		try {
			while ((st = br.readLine()) != null) {
				if (st.contains(arg)) {
					config = st;
					break;
				}
			}
		} catch (IOException e) {}

		config = config.replaceAll(arg, "").replaceAll("default=", "");
		return config;
	}

	private static void basicWrite(String arg, Object obj) {
		arg = arg.replaceAll("=", "");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(path)));
		} catch (FileNotFoundException e1) {}
		String st;
		try {
			HashMap<Integer, String> lines = new HashMap<>();
			int count = 0;
			int current = 0;
			boolean find = false;
			while ((st = br.readLine()) != null) {
				System.out.println(count);
				lines.put(count, st);
				if (st.contains(arg)) {
					current = count;
					find = true;
				}
				count++;
			}
			String newconf = arg + "=" + obj.toString();
			if (!find) current = lines.size() + 1;
			lines.remove(current);
			lines.put(current, newconf);
			List<String> configlines = new ArrayList<>();
			lines.values().forEach(str -> configlines.add(str));
			String configline = createConfigLineArray(configlines);
			FileWriter writer;
			writer = new FileWriter(new File(path));
			writer.write(configline);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean getBoolean(String arg) {
		return Boolean.valueOf(basic(arg));
	}

	public static void setBoolean(String arg, Boolean value) {
		basicWrite(arg, value);
	}

	public static String getString(String arg) {
		return basic(arg);
	}

	public static void setString(String arg, String value) {
		basicWrite(arg, value);
	}

	public static Integer getInteger(String arg) {
		return Integer.valueOf(basic(arg));
	}

	public static void setInteger(String arg, Integer value) {
		basicWrite(arg, value);
	}
}
package me.deadorfd.videos.utils.files.info;

import me.deadorfd.videos.utils.Utils;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.files.info
 * @Date 09.05.2024
 * @Time 02:51:32
 */
public class VideoLoader {

	private static final Logger log = LoggerFactory.getLogger(VideoLoader.class);
	private Long duration;
	private Integer width, height;
	private Double frameRate;
	private String img;

	private Integer getFrames(int frames) {
		if (frames > 1260) return 1260;
		if (frames > 560) return 560;
		if (frames > 300) return 300;
		return frames - 1;
	}

	public VideoLoader(VideoInfo videoFileInfo) {
		avutil.av_log_set_level(avutil.AV_LOG_QUIET);
		Instant start = Instant.now();
		File dump = new File(System.getProperty("java.io.tmpdir") + "/videos/" + videoFileInfo.getBaseFile().getFile().getName() + ".png");
		if (!dump.getParentFile().exists()) dump.getParentFile().mkdirs();
		try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFileInfo.getBaseFile().getFile())) {
			grabber.start();
			width = grabber.getImageWidth();
			height = grabber.getImageHeight();
			duration = grabber.getLengthInTime() / (1000 * 1000);
			frameRate = grabber.getVideoFrameRate();
			if (!dump.exists()) {
				Java2DFrameConverter converter = new Java2DFrameConverter();
				Frame frame;
				int frames = grabber.getLengthInVideoFrames();
				grabber.setFrameNumber(getFrames(frames));
				frame = grabber.grabImage();
				BufferedImage bi = converter.convert(frame);
				if (bi == null) {
					grabber.setFrameNumber(frames / 2);
					frame = grabber.grabFrame();
					bi = converter.convert(frame);
				}
				if (bi != null) {
					if (!dump.exists()) try {
						dump.createNewFile();
					} catch (IOException e) {
						log.error("An error occurred during video loading", e);
					}
					ImageIO.write(bi, "png", dump);
					img = dump.getPath();
				} else img = null;
			} else img = dump.getPath();
			grabber.stop();
			Instant end = Instant.now();
			Duration duration = Duration.between(start, end);
			System.out.println("Successfully loaded video: " + videoFileInfo.getBaseFile().getName() + " . (Took " + duration.toSeconds() + "s ("
					+ duration.toMillis() + " ms))");
		} catch (IOException e) {
			log.error("An error occurred during video loading", e);
		}
	}

	public Integer getWidth() {
		return width;
	}

	public Integer getHeight() {
		return height;
	}

	public void openImage() {
		Utils.openImageFrame(img);
	}

	public String getFrame() {
		if (img == null) return null;
		return img.replace('\\', '/');
	}

	public Long getRawDuration() {
		return duration;
	}

	private String getFormat(long time) {
		return (("" + time).length() > 1 ? "" + time : "0" + time);
	}

	public String getDuration() {
		long minutes = TimeUnit.SECONDS.toMinutes(duration) - (TimeUnit.SECONDS.toHours(duration) * 60);
		long hours = TimeUnit.SECONDS.toHours(duration);
		long seconds = TimeUnit.SECONDS.toSeconds(duration) - (TimeUnit.SECONDS.toMinutes(duration) * 60);
		if (hours >= 1) return getFormat(hours) + ":" + getFormat(minutes) + ":" + getFormat(seconds);
		if (minutes >= 1) return "00:" + getFormat(minutes) + ":" + getFormat(seconds);
		if (minutes == 0) return "00:00:" + getFormat(seconds);
		return "00:00:00";
	}

	public String getFrameRate() {
		DecimalFormat decimalFormat = new DecimalFormat("##.##");
		return decimalFormat.format(frameRate);
	}

	public Double getRawFrameRate() {
		return frameRate;
	}

}

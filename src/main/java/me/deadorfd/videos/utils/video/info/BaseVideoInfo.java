package me.deadorfd.videos.utils.video.info;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import me.deadorfd.videos.utils.Utils;
import me.deadorfd.videos.utils.video.BaseVideo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils
 * @Date 19.11.2023
 * @Time 23:01:33
 */
public abstract class BaseVideoInfo {

	private Long duration;
	private Integer width, height;
	private Double frameRate;
	private String img;
	private BaseVideo video;
	public static ArrayList<BaseVideoInfo> infos = new ArrayList<>();

	private Integer getInfosInt(BaseVideoInfo info) {
		Integer i = 0;
		for (BaseVideoInfo vInfos : infos) {
			if (vInfos.getVideo() == info.getVideo()) break;
			i++;
		}
		return i;
	}

	public BaseVideoInfo(BaseVideo video) {
		if (!infos.contains(this)) {
			this.video = video;
			avutil.av_log_set_level(avutil.AV_LOG_QUIET);
			FFmpegFrameGrabber g = new FFmpegFrameGrabber(video.getFile());
			String name = video.getFile().getName();
			File dump = new File(
					System.getProperty("java.io.tmpdir") + "/videos/" + video.getFile().getName() + ".png");
			if (!dump.getParentFile().exists()) dump.getParentFile().mkdir();
			if (!dump.exists()) try {
				dump.createNewFile();
			} catch (IOException e3) {
				e3.printStackTrace();
			}
			try {
				g.start();
				width = g.getImageWidth();
				height = g.getImageHeight();
				duration = g.getLengthInTime() / (1000 * 1000);
				frameRate = g.getVideoFrameRate();
				Java2DFrameConverter converter = new Java2DFrameConverter();
				Frame frame = null;
				int frames = g.getLengthInVideoFrames();
				if (frames > 1260) {
					g.setFrameNumber(1260);
				} else if (frames > 560) {
					g.setFrameNumber(560);
				} else if (frames > 300) {
					g.setFrameNumber(300);
				} else
					g.setFrameNumber(frames - 1);
				frame = g.grabImage();
				BufferedImage bi = converter.convert(frame);
				if (bi == null) {
					g.setFrameNumber(frames / 2);
					frame = g.grabFrame();
					bi = converter.convert(frame);
				}
				ImageIO.write(bi, "png", dump);
				img = dump.getPath();
				g.stop();
			} catch (IOException e) {
				e.printStackTrace();
			}
			infos.add(this);
		} else {
			BaseVideoInfo info = infos.get(getInfosInt(this));
			this.duration = info.getRawDuration();
			this.width = info.getWidth();
			this.height = info.getHeight();
			this.img = info.getFrame();
			this.frameRate = info.getRawFrameRate();
			this.video = info.getVideo();
		}
	}

	public String getFrameRate() {
		DecimalFormat decimalFormat = new DecimalFormat("##.##");
		return decimalFormat.format(frameRate);
	}

	public Double getRawFrameRate() {
		return frameRate;
	}

	private String getFormat(long time) {
		return (("" + time).length() > 1 ? "" + time : "0" + time);
	}

	public Long getRawDuration() {
		return duration;
	}

	public String getDuration() {
		long minutes = TimeUnit.SECONDS.toMinutes(duration) - (TimeUnit.SECONDS.toHours(duration) * 60);
		long hours = TimeUnit.SECONDS.toHours(duration);
		long seconds = TimeUnit.SECONDS.toSeconds(duration) - (TimeUnit.SECONDS.toMinutes(duration) * 60);
		if (hours >= 1)
			return "Länge: " + getFormat(hours) + ":" + getFormat(minutes) + ":" + getFormat(seconds) + "";
		if (minutes >= 1) return "Länge: 00:" + getFormat(minutes) + ":" + getFormat(seconds) + "";
		if (minutes == 0) return "Länge: 00:00:" + getFormat(seconds) + "";
		return "Länge: 00:00:00";
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
		return img.replace('\\', '/');
	}

	public BaseVideo getVideo() {
		return video;
	}
}

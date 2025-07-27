package com.Guess.ReportsPlus.util.Misc;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.Guess.ReportsPlus.config.ConfigReader;

public class AudioUtil {
	public static final ExecutorService audioExecutor = Executors.newCachedThreadPool();
	private static final int BUFFER_SIZE = 128000;

	public static void playSound(String filename) {
		try {
			if (ConfigReader.configRead("uiSettings", "enableSounds").equalsIgnoreCase("true")) {
				File soundFile = new File(filename);
				if (!soundFile.exists()) {
					logWarn("Sound file does not exist: " + filename);
					return;
				}
				audioExecutor.submit(() -> {
					try {
						AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
						AudioFormat audioFormat = audioStream.getFormat();
						SourceDataLine sourceLine = createSourceLine(audioFormat);
						sourceLine.start();
						byte[] buffer = new byte[BUFFER_SIZE];
						int bytesRead;
						while ((bytesRead = audioStream.read(buffer, 0, buffer.length)) != -1) {
							sourceLine.write(buffer, 0, bytesRead);
						}
						sourceLine.drain();
						closeResources(sourceLine, audioStream);
					} catch (Exception e) {
						logError("Error playing sound: ", e);
					}
				});
			}
		} catch (IOException e) {
			logError("Could not read enableSounds config: ", e);
		}
	}

	private static SourceDataLine createSourceLine(AudioFormat audioFormat) throws LineUnavailableException {
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
		sourceLine.open(audioFormat);
		return sourceLine;
	}

	private static void closeResources(SourceDataLine sourceLine, AudioInputStream audioStream) {
		sourceLine.close();
		try {
			audioStream.close();
		} catch (IOException e) {
			logError("Error with closing audio resources (2): ", e);
		}
	}
}

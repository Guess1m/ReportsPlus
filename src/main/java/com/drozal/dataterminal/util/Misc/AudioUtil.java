package com.drozal.dataterminal.util.Misc;

import com.drozal.dataterminal.config.ConfigReader;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;

public class AudioUtil {
	private static final int BUFFER_SIZE = 128000;
	
	public static void playSound(String filename) {
		try {
			if (ConfigReader.configRead("uiSettings", "enableSounds").equalsIgnoreCase("true")) {
				File soundFile = new File(filename);
				
				if (!soundFile.exists()) {
					log("Sound file does not exist: " + filename, LogUtils.Severity.WARN);
					return;
				}
				
				new Thread(() -> {
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
						e.printStackTrace();
					}
				}).start();
			}
		} catch (IOException e) {
			System.err.println("Could not read enableSounds config: " + e.getMessage());
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
			e.printStackTrace();
		}
	}
}

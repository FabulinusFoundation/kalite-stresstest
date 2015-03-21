package org.fabulinus.client;

import org.fabulinus.input.Resources;
import org.fabulinus.logging.LogLevel;
import org.fabulinus.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by Timon on 12.03.2015.
 */
public class Client extends Thread{
    private final String host;
    private final int port;
    private final Resources resources;
    private final Logger logger;
    private final long defaultTimeout;

    public Client(String host, int port, Resources resources, Logger logger, long defaultTimeout){
        this.host = host;
        this.port = port;
        this.resources = resources;
        this.logger = logger;
        this.defaultTimeout = defaultTimeout;
    }

    @Override
    public void run() {
        logger.log("Starting new " + toString(), "", LogLevel.INFO);
        while(!isInterrupted()){
            int duration = accessContent();
            if (duration < 0) {
                logger.log(toString() + ": Error while accessing content!", "", LogLevel.ERROR);
            } else {
                logger.log(toString() + ": " + "response required " + duration + "s.", "",
                        (duration < 6 ? LogLevel.INFO : LogLevel.WARN));
            }
            long rndTimeout = calculateTimeout();
            logger.log(toString() + " sleeping for " + rndTimeout/1000 + "s.", "", LogLevel.DEBUG);
            trySleep(rndTimeout);
        }
    }

    private int accessContent() {
        int rnd = (int) (Math.floor(Math.random()*3) + 1); // [1..4)
        switch (rnd) {
            case 1:
                logger.log(toString() + " browsing page.", "", LogLevel.INFO);
                return browsePage();
            case 2:
                logger.log(toString() + " streaming video.", "", LogLevel.INFO);
                return streamVideo();
            case 3:
                logger.log(toString() + " browsing exercise.", "", LogLevel.INFO);
                return browseExercise();
            default:
                assert(false);
        }
        return -1;
    }

    private long calculateTimeout() {
        long offset = (long) (defaultTimeout * 0.125);
        long rndTimeout = (long) ((Math.random()*(defaultTimeout*0.25)));
        return defaultTimeout - offset + rndTimeout;
    }

    //------------------------------- Page -------------------------------//
    private int browsePage() {
        long start = new Date().getTime();
        try {
            URL url = getRandomPageUrl();
            InputStream stream = url.openStream();
            long bytes = readStream(stream);
            logger.log(toString() + " received " + formatBytes(bytes) + " from page.", "", LogLevel.INFO);
            stream.close();
        } catch (Exception e) {
            logger.log(toString() + " " + e.getLocalizedMessage(), "", LogLevel.ERROR);
            return -1;
        }
        long end = new Date().getTime();
        return (int) ((end-start)/1000);
    }

    private URL getRandomPageUrl() throws MalformedURLException {
        return new URL("http", host, port, getRandomPage());
    }

    private String getRandomPage(){
        int index = 0;
        return resources.getPages().get(index);
    }

    //------------------------------- Video -------------------------------//
    private int streamVideo() {
        long start = new Date().getTime(), end;
        try {
            URL url = getRandomVideoUrl();
            InputStream stream = url.openStream();
            long startS = new Date().getTime();
            long bytes = readStream(stream);
            long endS = new Date().getTime();
            int watchTime = calculateRandomWatchTimeInSeconds(startS, endS, bytes);
            logger.log(toString() + " received " + formatBytes(bytes) + " from video. Watching " + watchTime + "s.", "", LogLevel.INFO);
            trySleep(watchTime * 1000);
            stream.close();
            end = new Date().getTime() - watchTime*1000;
        } catch (Exception e) {
            logger.log(toString() + " " + e.getLocalizedMessage(), "", LogLevel.ERROR);
            return -1;
        }
        return (int) ((end-start) / 1000);
    }

    private URL getRandomVideoUrl() throws MalformedURLException {
        return new URL("http", host, port, getRandomVideo());
    }

    private String getRandomVideo(){
        int index = 0;
        return resources.getVideos().get(index);
    }

    //------------------------------- Exercise -------------------------------//
    private int browseExercise() {
        long start = new Date().getTime();
        try {
            URL url = getRandomExerciseUrl();
            InputStream stream = url.openStream();
            long bytes = readStream(stream);
            logger.log(toString() + " received " + formatBytes(bytes) + " from exercise.", "", LogLevel.INFO);
            stream.close();
        } catch (Exception e) {
            logger.log(toString() + " " + e.getLocalizedMessage(), "", LogLevel.ERROR);
            return -1;
        }
        long end = new Date().getTime();
        return (int) ((end - start) / 1000);
    }

    private URL getRandomExerciseUrl() throws MalformedURLException{
        return new URL("http", host, port, getRandomExercise());
    }

    private String getRandomExercise() {
        int index = 0;
        return resources.getExercises().get(index);
    }

    //------------------------------- General -------------------------------//
    private long readStream(InputStream stream) throws IOException {
        int count = 0;
        int read;
        do {
            read = stream.read();
            count++;
        } while (read != -1);
        return count;
    }

    private String formatBytes(long bytes){
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        char pre = "KMGTPE".charAt(exp-1);
        return String.format("%.1f%sB", bytes / Math.pow(unit, exp), pre);
    }

    private long getVideoDurationInSeconds(long bytes){
        return bytes / (12 * 1024); //approx.
    }

    private int calculateRandomWatchTimeInSeconds(long start, long end, long bytes){
        long videoDuration = end-start + getVideoDurationInSeconds(bytes)* 1000;
        double rndFactor = Math.random();
        return (int) ((videoDuration * rndFactor) / 1000);
    }

    private void trySleep(long timeout){
        try {
            sleep(timeout);
        } catch (InterruptedException e) {
            logger.log(toString() + " interrupted.", "", LogLevel.INFO);
            interrupt();
        }
    }

    @Override
    public String toString() {
        String id = String.valueOf(hashCode());
        int length = (id.length() >= 8 ? 8 : id.length());
        return "[Client:" + id.substring(0, length) + "]";
    }
}

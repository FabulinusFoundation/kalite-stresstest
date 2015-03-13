package org.fabulinus.client;

import org.fabulinus.logging.LogEntry;
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
    private final Logger logger;
    private final long defaultTimeout;

    public Client(String host, int port, Logger logger, long defaultTimeout){
        this.host = host;
        this.port = port;
        this.logger = logger;
        this.defaultTimeout = defaultTimeout;
    }

    @Override
    public void run() {
        logger.log("Starting new " + toString(), "", LogLevel.INFO);
        while(!isInterrupted()){
            Date before = new Date();
            accessContent();
            Date after = new Date();
            long elapsed = after.getTime()-before.getTime();
            if (elapsed > 5000) {
                logger.log(toString() + ": " + "response required " + elapsed + "ms.", "", LogLevel.WARN);
            }
            long rndTimeout = calculateTimeout();
            logger.log(toString() + " sleeping for " + rndTimeout + "ms.", "", LogLevel.DEBUG);
            try {
                sleep(rndTimeout);
            } catch (InterruptedException i) {
                logger.log(toString() + " interrupted.", "", LogLevel.INFO);
                interrupt();
            }
        }
    }

    private void accessContent() {
        int rnd = (int) (Math.floor(Math.random()*3) + 1); // [1..4)
        switch (rnd) {
            case 1:
                logger.log(toString() + " browsing page.", "", LogLevel.INFO);
                browsePage();
                return;
            case 2:
                logger.log(toString() + " streaming video.", "", LogLevel.INFO);
                streamVideo();
                return;
            case 3:
                logger.log(toString() + " browsing exercise.", "", LogLevel.INFO);
                browseExercise();
                return;
            default:
                assert(false);
        }
    }

    private long calculateTimeout() {
        long offset = (long) (defaultTimeout * 0.125);
        long rndTimeout = (long) ((Math.random()*(defaultTimeout*0.25)));
        return defaultTimeout - offset + rndTimeout;
    }

    //------------------------------- Page -------------------------------//
    private void browsePage() {
        try {
            URL url = getRandomPageUrl();
            InputStream stream = url.openStream();
            int bytes = readStream(stream);
            logger.log(toString() + " received " + formatBytes(bytes) + " from page.", "", LogLevel.INFO);
            stream.close();
        } catch (Exception e) {
            logger.log(toString() + " " + e.getLocalizedMessage(), "", LogLevel.ERROR);
        }
    }

    private URL getRandomPageUrl() throws MalformedURLException {
        return new URL("http", host, port, getRandomPage());
    }

    private String getRandomPage(){
        return "/math/arithmetic/";
    }

    //------------------------------- Video -------------------------------//
    private void streamVideo() {
        try {
            URL url = getRandomVideoUrl();
            InputStream stream = url.openStream();
            int bytes = readStream(stream);
            logger.log(toString() + " received " + formatBytes(bytes) + " from video.", "", LogLevel.INFO);
            stream.close();
        } catch (Exception e) {
            logger.log(toString() + " " + e.getLocalizedMessage(), "", LogLevel.ERROR);
        }
    }

    private URL getRandomVideoUrl() throws MalformedURLException {
        return new URL("http", host, port, getRandomVideo());
    }

    private String getRandomVideo(){
        return "/math/arithmetic/addition-subtraction/basic_addition/v/basic-addition/";
    }

    //------------------------------- Exercise -------------------------------//
    private void browseExercise() {
        try {
            URL url = getRandomExerciseUrl();
            InputStream stream = url.openStream();
            int bytes = readStream(stream);
            logger.log(toString() + " received " + formatBytes(bytes) + " from exercise.", "", LogLevel.INFO);
            stream.close();
        } catch (Exception e) {
            logger.log(toString() + " " + e.getLocalizedMessage(), "", LogLevel.ERROR);
        }
    }

    private URL getRandomExerciseUrl() throws MalformedURLException{
        return new URL("http", host, port, getRandomExercise());
    }

    private String getRandomExercise() {
        return "/math/arithmetic/addition-subtraction/basic_addition/e/addition_1/";
    }

    //------------------------------- General -------------------------------//
    private int readStream(InputStream stream) throws IOException {
        int count = 0;
        int read;
        do {
            read = stream.read();
            count++;
        } while (read != -1);
        return count;
    }

    private String formatBytes(int bytes){
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        char pre = "kmgtpe".charAt(exp-1);
        return String.format("%.1f%sB", bytes / Math.pow(unit, exp), pre);
    }

    @Override
    public String toString() {
        String id = String.valueOf(hashCode());
        int length = (id.length() >= 8 ? 8 : id.length());
        return "[Client:" + id.substring(0, length) + "]";
    }
}

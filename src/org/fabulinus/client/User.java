package org.fabulinus.client;

import org.fabulinus.logging.LogLevel;
import org.fabulinus.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;

/**
 * Created by Timon on 12.03.2015.
 */
public class User extends Thread{
    private final String HOST;
    private final int PORT;
    private final Logger LOGGER;

    public User(String host, int port, Logger logger) {
        this.HOST = removeProtocol(host);
        this.PORT = port;
        this.LOGGER = logger;
    }

    private String removeProtocol(String url) {
        String result = url.toLowerCase();
        if (result.startsWith("http://") || result.startsWith("https://")) {
            result = result.substring(url.indexOf("//") + 1);
        }
        return result;
    }

    @Override
    public void run() {
        LOGGER.log("Starting new user [" + toString() + "].", LogLevel.INFO);
        while(!isInterrupted()){
            accessContent();
            int timeout = (int) ((Math.random()*500) + 500);
            LOGGER.log("User [" + toString() + "] pausing for " + timeout + "ms.", LogLevel.INFO);
            try {
                sleep(timeout);
            } catch (InterruptedException i) {
                LOGGER.log("User [" + toString() + "] finished.", LogLevel.INFO);
                interrupt();
            }
        }
    }

    private void accessContent() {
        int rnd = (int) (Math.floor(Math.random()*3) + 1); // [1..4)
        switch (rnd) {
            case 1:
                LOGGER.log("User [" + toString() + "] browsing page.", LogLevel.INFO);
                browsePage();
                return;
            case 2:
                LOGGER.log("User [" + toString() + "] streaming video.", LogLevel.INFO);
                streamVideo();
                return;
            case 3:
                LOGGER.log("User [" + toString() + "] browsing exercise.", LogLevel.INFO);
                browseExercise();
                return;
            default:
                LOGGER.log("Invalid content integer generated.", LogLevel.DEBUG);
        }
    }

    //------------------------------- Page -------------------------------//
    private void browsePage() {
        try {
            URL url = getRandomPageUrl();
            InputStream stream = url.openStream();
            stream.read();
            stream.close();
        } catch (Exception e) {
            LOGGER.log("Exception while browsing a page. " + e.getLocalizedMessage(), LogLevel.ERROR);
        }
    }

    private URL getRandomPageUrl() throws MalformedURLException {
        return new URL("http", HOST, PORT, getRandomPage());
    }

    private String getRandomPage(){
        return "/math/arithmetic/";
    }

    //------------------------------- Video -------------------------------//
    private void streamVideo() {
        try {
            URL url = getRandomVideoUrl();
            InputStream stream = url.openStream();
            stream.read();
            stream.close();
        } catch (Exception e) {

        }
    }

    private URL getRandomVideoUrl() throws MalformedURLException {
        return new URL("http", HOST, PORT, getRandomVideo());
    }

    private String getRandomVideo(){
        return "/math/arithmetic/addition-subtraction/basic_addition/v/basic-addition/";
    }

    //------------------------------- Exercise -------------------------------//
    private void browseExercise() {
        try {
            URL url = getRandomExerciseUrl();
            InputStream stream = url.openStream();
            stream.read();
            stream.close();
        } catch (Exception e) {

        }
    }

    private URL getRandomExerciseUrl() throws MalformedURLException{
        return new URL("http", HOST, PORT, getRandomExercise());
    }

    private String getRandomExercise() {
        return "/math/arithmetic/addition-subtraction/basic_addition/e/addition_1/";
    }

    @Override
    public String toString() {
        String id = String.valueOf(hashCode());
        int length = (id.length() >= 8 ? 8 : id.length());
        return id.substring(0, length);
    }
}

package org.fabulinus.input;

import org.fabulinus.logging.LogLevel;
import org.fabulinus.logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Timon on 21.03.2015.
 */
public class ResourceFileReader {
    private final File resourceFile;
    private final Logger logger;

    public ResourceFileReader(String file, Logger logger) {
        this.resourceFile = new File(file);
        this.logger = logger;
    }

    public Resources readResources(){
        Resources result = new Resources();
        String input = readFile();
        for (String video : readVideos(input)){
            if (!video.trim().isEmpty()) {
                result.addVideo(video.trim());
            }
        }
        for (String exercise : readExercises(input)){
            if (!exercise.trim().isEmpty()) {
                result.addExercise(exercise.trim());
            }
        }
        for (String page : readPages(input)){
            if (!page.trim().isEmpty()) {
                result.addPage(page.trim());
            }
        }
        return result;
    }

    private String readFile(){
        StringBuffer input = new StringBuffer();
        try {
            FileInputStream stream = new FileInputStream(resourceFile);
            int next = stream.read();
            while (next != -1) {
                input.append((char) next);
                next = stream.read();
            }
        } catch (FileNotFoundException fnf){
            logger.log("Resource file not found!", fnf.getLocalizedMessage(), LogLevel.ERROR);
        } catch (IOException io) {
            logger.log("Error while reading resource file!", io.getLocalizedMessage(), LogLevel.ERROR);
        }
        return input.toString();
    }

    private String[] readVideos(String input){
        String start = "<videos>";
        String end = "</videos>";
        int vBegin = input.indexOf(start) + start.length();
        int vEnd = input.lastIndexOf(end) - end.length();
        String sub = input.substring(vBegin, vEnd);
        return sub.split("\n");
    }

    private String[] readExercises(String input){
        String start = "<exercises>";
        String end = "</exercises>";
        int vBegin = input.indexOf(start) + start.length();
        int vEnd = input.lastIndexOf(end) - end.length();
        String sub = input.substring(vBegin, vEnd);
        return sub.split("\n");
    }

    private String[] readPages(String input){
        String start = "<pages>";
        String end = "</pages>";
        int vBegin = input.indexOf(start) + start.length();
        int vEnd = input.lastIndexOf(end) - end.length();
        String sub = input.substring(vBegin, vEnd);
        return sub.split("\n");
    }
}

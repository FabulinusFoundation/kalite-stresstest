package org.fabulinus.input;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timon on 21.03.2015.
 */
public class Resources {
    private final List<String> videos;
    private final List<String> exercises;
    private final List<String> pages;

    public Resources() {
        videos = new ArrayList<>();
        exercises = new ArrayList<>();
        pages = new ArrayList<>();
    }

    public void addVideo(String url){
        this.videos.add(url);
    }

    public void addExercise(String url) {
        this.exercises.add(url);
    }

    public void addPage(String url){
        this.pages.add(url);
    }

    public List<String> getVideos() {
        return videos;
    }

    public List<String> getExercises() {
        return exercises;
    }

    public List<String> getPages() {
        return pages;
    }
}

package com.wink.livemall.admin.util.idCardUtils;

public class KeyDetail {
    private String words;
    private Location location;

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "{" +
                "words='" + words + '\'' +
                ", location=" + location +
                '}';
    }
}

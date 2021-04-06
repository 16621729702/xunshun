package com.wink.livemall.admin.util.idCardUtils.Bean;


import java.util.List;

public class Hits {

    private int probability;
    private String datasetName;
    private List<String> words;
    public void setProbability(int probability) {
        this.probability = probability;
    }
    public int getProbability() {
        return probability;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }
    public String getDatasetName() {
        return datasetName;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
    public List<String> getWords() {
        return words;
    }

}
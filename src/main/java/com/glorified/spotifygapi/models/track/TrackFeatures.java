package com.glorified.spotifygapi.models.track;

public class TrackFeatures {
    private double id;
    private String trackID;
    private String analysisID;
    private double accousticness;
    private double dancebility;
    private double energy;
    private double instrumentalness;
    private double key;
    private double liveness;
    private double loudness;
    private double modality;
    private double speechiness;
    private double bpm;
    private double timeSignature;
    private double valence;
    private String href;

    public TrackFeatures() {
    }

    public TrackFeatures(double id, String trackID, String analysisID, double accousticness, double dancebility, double energy, double instrumentalness, double key, double liveness, double loudness, double modality, double speechiness, double bpm, double timeSignature, double valence, String href) {
        this.id = id;
        this.trackID = trackID;
        this.analysisID = analysisID;
        this.accousticness = accousticness;
        this.dancebility = dancebility;
        this.energy = energy;
        this.instrumentalness = instrumentalness;
        this.key = key;
        this.liveness = liveness;
        this.loudness = loudness;
        this.modality = modality;
        this.speechiness = speechiness;
        this.bpm = bpm;
        this.timeSignature = timeSignature;
        this.valence = valence;
        this.href = href;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getTrackID() {
        return trackID;
    }

    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }

    public String getAnalysisID() {
        return analysisID;
    }

    public void setAnalysisID(String analysisID) {
        this.analysisID = analysisID;
    }

    public double getAccousticness() {
        return accousticness;
    }

    public void setAccousticness(double accousticness) {
        this.accousticness = accousticness;
    }

    public double getDancebility() {
        return dancebility;
    }

    public void setDancebility(double dancebility) {
        this.dancebility = dancebility;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getInstrumentalness() {
        return instrumentalness;
    }

    public void setInstrumentalness(double instrumentalness) {
        this.instrumentalness = instrumentalness;
    }

    public double getKey() {
        return key;
    }

    public void setKey(double key) {
        this.key = key;
    }

    public double getLiveness() {
        return liveness;
    }

    public void setLiveness(double liveness) {
        this.liveness = liveness;
    }

    public double getLoudness() {
        return loudness;
    }

    public void setLoudness(double loudness) {
        this.loudness = loudness;
    }

    public double getModality() {
        return modality;
    }

    public void setModality(double modality) {
        this.modality = modality;
    }

    public double getSpeechiness() {
        return speechiness;
    }

    public void setSpeechiness(double speechiness) {
        this.speechiness = speechiness;
    }

    public double getBpm() {
        return bpm;
    }

    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    public double getTimeSignature() {
        return timeSignature;
    }

    public void setTimeSignature(double timeSignature) {
        this.timeSignature = timeSignature;
    }

    public double getValence() {
        return valence;
    }

    public void setValence(double valence) {
        this.valence = valence;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}

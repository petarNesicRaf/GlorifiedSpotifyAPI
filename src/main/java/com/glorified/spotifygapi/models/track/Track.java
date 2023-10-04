package com.glorified.spotifygapi.models.track;

public class Track {
    private int id;
    private String addedAt;
    private String addedByHref;
    private String addedByID;
    private String duration;
    private String trackHref;
    private String trackID;
    private int isLocal;
    private String trackName;
    private int popularity;
    private String videoThumbnail;
    private String previewUrl;
    private String artistsNames;
    private String artistsIDs;
    private String albumType;
    private String albumName;
    private String albumID;
    private String imageLarge;
    private String imageMedium;
    private String imageSmall;

    public Track() {
    }

    public Track(int id, String addedAt, String addedByHref, String addedByID, String duration, String trackHref, String trackID, int isLocal, String trackName, int popularity, String videoThumbnail, String previewUrl, String artistsNames, String artistsIDs, String albumType, String albumName, String albumID, String imageLarge, String imageMedium, String imageSmall) {
        this.id = id;
        this.addedAt = addedAt;
        this.addedByHref = addedByHref;
        this.addedByID = addedByID;
        this.duration = duration;
        this.trackHref = trackHref;
        this.trackID = trackID;
        this.isLocal = isLocal;
        this.trackName = trackName;
        this.popularity = popularity;
        this.videoThumbnail = videoThumbnail;
        this.previewUrl = previewUrl;
        this.artistsNames = artistsNames;
        this.artistsIDs = artistsIDs;
        this.albumType = albumType;
        this.albumName = albumName;
        this.albumID = albumID;
        this.imageLarge = imageLarge;
        this.imageMedium = imageMedium;
        this.imageSmall = imageSmall;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

    public String getAddedByHref() {
        return addedByHref;
    }

    public void setAddedByHref(String addedByHref) {
        this.addedByHref = addedByHref;
    }

    public String getAddedByID() {
        return addedByID;
    }

    public void setAddedByID(String addedByID) {
        this.addedByID = addedByID;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTrackHref() {
        return trackHref;
    }

    public void setTrackHref(String trackHref) {
        this.trackHref = trackHref;
    }

    public int getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(int isLocal) {
        this.isLocal = isLocal;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getArtistsNames() {
        return artistsNames;
    }

    public void setArtistsNames(String artistsNames) {
        this.artistsNames = artistsNames;
    }

    public String getArtistsIDs() {
        return artistsIDs;
    }

    public void setArtistsIDs(String artistsIDs) {
        this.artistsIDs = artistsIDs;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getImageLarge() {
        return imageLarge;
    }

    public void setImageLarge(String imageLarge) {
        this.imageLarge = imageLarge;
    }

    public String getImageMedium() {
        return imageMedium;
    }

    public void setImageMedium(String imageMedium) {
        this.imageMedium = imageMedium;
    }

    public String getImageSmall() {
        return imageSmall;
    }

    public void setImageSmall(String imageSmall) {
        this.imageSmall = imageSmall;
    }

    public String getTrackID() {
        return trackID;
    }

    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }
}

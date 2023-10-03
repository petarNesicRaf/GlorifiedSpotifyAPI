package com.glorified.spotifygapi.models.playlist;
public class Playlist {
    //todo validacija annotacije
    private int id;
    private String name;
    private String description;
    private String playlistId;
    private String userId;
    private String ownerName;
    private String tracks;
    private int numOfTracks;
    private String imageLarge;
    private String imageMedium;
    private String imageSmall;
    private int collaborative;

    public Playlist() {
    }

    public int getNumOfTracks() {
        return numOfTracks;
    }

    public void setNumOfTracks(int numOfTracks) {
        this.numOfTracks = numOfTracks;
    }

    public Playlist(int id, String name, String description, String playlistId, String userId, String ownerName, String tracks, int numOfTracks, String imageLarge, String imageMedium, String imageSmall, int collaborative) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.playlistId = playlistId;
        this.userId = userId;
        this.ownerName = ownerName;
        this.tracks = tracks;
        this.numOfTracks = numOfTracks;
        this.imageLarge = imageLarge;
        this.imageMedium = imageMedium;
        this.imageSmall = imageSmall;
        this.collaborative = collaborative;
    }

    public int getCollaborative() {
        return collaborative;
    }

    public int getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTracks() {
        return tracks;
    }

    public void setTracks(String tracks) {
        this.tracks = tracks;
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

    public int isCollaborative() {
        return collaborative;
    }

    public void setCollaborative(int collaborative) {
        this.collaborative = collaborative;
    }
}

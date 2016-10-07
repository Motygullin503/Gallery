package ru.ksu.motygullin.gallery;

/**
 * @author Rishad Mustafaev
 */

public class VkFriend {

    private String name;
    private String surname;
    private String smallPhotoUrl;
    private String maxPhotoUrl;
    private boolean isOnline;

    public VkFriend() {
    }

    public VkFriend(String name, String surname, String smallPhotoUrl, String maxPhotoUrl, boolean isOnline) {
        this.name = name;
        this.surname = surname;
        this.smallPhotoUrl = smallPhotoUrl;
        this.maxPhotoUrl  = maxPhotoUrl;
        this.isOnline = isOnline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSmallPhotoUrl() {
        return smallPhotoUrl;
    }

    public void setSmallPhotoUrl(String smallPhotoUrl) {
        this.smallPhotoUrl = smallPhotoUrl;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getMaxPhotoUrl() {
        return maxPhotoUrl;
    }

    public void setMaxPhotoUrl(String maxPhotoUrl) {
        this.maxPhotoUrl = maxPhotoUrl;
    }
}

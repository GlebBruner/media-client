package nure.ua.mediaclient.model;

import java.util.Date;
import java.util.Set;

import nure.ua.mediaclient.model.domain.Point;

public class OrderDTO {

    public static final int NO_LIKES = 0;
    public static final int NO_DISLIKES = 0;

    private String title;
    private boolean isPaid;
    private boolean isPrivate;
    private String description;
    private int photoCount;
    private int videoCount;
    private Set<String> hashtags;
    private Set<String> categories;
    private int likes;
    private int dislikes;
    private Point location;
    private Date creationDate;
    private Date deadline;
    private float money;

    public OrderDTO() {
    }

    public OrderDTO(String title,
                    boolean isPaid,
                    boolean isPrivate,
                    String description,
                    int photoCount,
                    int videoCount,
                    Set<String> hashtags,
                    Set<String> categories,
                    int likes,
                    int dislikes,
                    Point location,
                    Date creationDate,
                    Date deadline,
                    float money) {
        this.title = title;
        this.isPaid = isPaid;
        this.isPrivate = isPrivate;
        this.description = description;
        this.photoCount = photoCount;
        this.videoCount = videoCount;
        this.hashtags = hashtags;
        this.categories = categories;
        this.likes = likes;
        this.dislikes = dislikes;
        this.location = location;
        this.creationDate = creationDate;
        this.deadline = deadline;
        this.money = money;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }

    public Set<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(Set<String> hashtags) {
        this.hashtags = hashtags;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public nure.ua.mediaclient.model.domain.Point getLocation() {
        return location;
    }

    public void setLocation(nure.ua.mediaclient.model.domain.Point location) {
        this.location = location;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }
}

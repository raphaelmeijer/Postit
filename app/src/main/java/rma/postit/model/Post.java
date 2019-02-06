package rma.postit.model;

import java.util.UUID;

public class Post {
    private String id = UUID.randomUUID().toString();
    private String title = "";
    private String description = "";

    public Post(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

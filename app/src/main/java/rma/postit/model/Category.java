package rma.postit.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Category {
    private String id = UUID.randomUUID().toString();
    private String name = "";
    private String description = "";
    private List<Post> posts = new ArrayList<>();

    public Category(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}

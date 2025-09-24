package com.example.jwt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "blogs")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Blog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogid;
    
    @Column(nullable = false)
    private String blogTitle;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String blogContent;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"blogs", "hibernateLazyInitializer", "handler"})
    private User user;
    
    // Constructors
    public Blog() {}
    
    public Blog(String blogTitle, String blogContent) {
        this.blogTitle = blogTitle;
        this.blogContent = blogContent;
    }
    
    // Getters and Setters
    public Long getBlogid() {
        return blogid;
    }
    
    public void setBlogid(Long blogid) {
        this.blogid = blogid;
    }
    
    public String getBlogTitle() {
        return blogTitle;
    }
    
    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }
    
    public String getBlogContent() {
        return blogContent;
    }
    
    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}
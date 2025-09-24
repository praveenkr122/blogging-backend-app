package com.example.jwt.DTO;

import com.example.jwt.entity.Blog;

public class BlogDTO {
    private Long blogid;
    private String blogTitle;
    private String blogContent;
    private String userEmail; // Optional: include user email instead of full user object
    
    // Default constructor
    public BlogDTO() {}
    
    // Constructor from Blog entity
    public BlogDTO(Blog blog) {
        this.blogid = blog.getBlogid();
        this.blogTitle = blog.getBlogTitle();
        this.blogContent = blog.getBlogContent();
        this.userEmail = blog.getUser() != null ? blog.getUser().getEmail() : null;
    }
    
    // Constructor with all fields
    public BlogDTO(Long blogid, String blogTitle, String blogContent, String userEmail) {
        this.blogid = blogid;
        this.blogTitle = blogTitle;
        this.blogContent = blogContent;
        this.userEmail = userEmail;
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
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    @Override
    public String toString() {
        return "BlogDTO{" +
                "blogid=" + blogid +
                ", blogTitle='" + blogTitle + '\'' +
                ", blogContent='" + blogContent + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
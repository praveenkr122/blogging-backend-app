package com.example.jwt.service;

import com.example.jwt.entity.Blog;
import com.example.jwt.entity.User;
import com.example.jwt.repository.BlogRepository;
import com.example.jwt.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    public BlogService(BlogRepository blogRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    // Save blog with user attached
    public Blog saveBlog(Blog blog, String userEmail) {
        System.out.println("Saving blog for user: " + userEmail);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        blog.setUser(user);
        Blog savedBlog = blogRepository.save(blog);
        System.out.println("Blog saved with ID: " + savedBlog.getBlogid());
        return savedBlog;
    }

    // Get all blogs for a user
    public List<Blog> getAllBlogsByUser(String userEmail) {
        System.out.println("Fetching blogs for user: " + userEmail);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Blog> blogs = blogRepository.findByUser(user);
        System.out.println("Found " + blogs.size() + " blogs for user: " + userEmail);
        return blogs;
    }

    public Blog updateBlog(Long blogId, Blog updatedBlog, String email) {
        System.out.println("Updating blog ID: " + blogId + " for user: " + email);
        System.out.println("Updated blog title: " + updatedBlog.getBlogTitle());
        System.out.println("Updated blog content: " + updatedBlog.getBlogContent());
        
        Optional<Blog> existing = blogRepository.findById(blogId);
        if (existing.isPresent()) {
            Blog blog = existing.get();
            System.out.println("Blog found, owner email: " + blog.getUser().getEmail());
            
            if (!blog.getUser().getEmail().equals(email)) {
                System.out.println("Unauthorized access attempt");
                return null;
            }
            
            // Update the blog fields
            System.out.println("Before update - Title: " + blog.getBlogTitle() + ", Content: " + blog.getBlogContent());
            blog.setBlogTitle(updatedBlog.getBlogTitle());
            blog.setBlogContent(updatedBlog.getBlogContent());
            System.out.println("After update - Title: " + blog.getBlogTitle() + ", Content: " + blog.getBlogContent());
            
            try {
                Blog savedBlog = blogRepository.save(blog);
                System.out.println("Blog updated successfully with ID: " + savedBlog.getBlogid());
                return savedBlog;
            } catch (Exception e) {
                System.err.println("Error saving updated blog: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("Blog with ID " + blogId + " not found");
            return null;
        }
    }

    // Delete blog only if it belongs to the user
    public boolean deleteBlog(Long blogId, String userEmail) {
        System.out.println("Deleting blog ID: " + blogId + " for user: " + userEmail);
        Optional<Blog> existingBlogOpt = blogRepository.findById(blogId);
        if (existingBlogOpt.isEmpty()) {
            System.out.println("Blog with ID " + blogId + " not found for deletion");
            return false;
        }

        Blog existingBlog = existingBlogOpt.get();
        if (!existingBlog.getUser().getEmail().equals(userEmail)) {
            System.out.println("Unauthorized delete attempt for blog ID: " + blogId);
            return false;
        }

        try {
            blogRepository.delete(existingBlog);
            System.out.println("Blog with ID " + blogId + " deleted successfully");
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting blog: " + e.getMessage());
            return false;
        }
    }
}
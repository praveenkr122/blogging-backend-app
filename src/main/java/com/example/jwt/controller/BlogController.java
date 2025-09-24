package com.example.jwt.controller;

import com.example.jwt.DTO.ApiResponse;
import com.example.jwt.DTO.BlogDTO;
import com.example.jwt.entity.Blog;
import com.example.jwt.repository.BlogRepository;
import com.example.jwt.security.JwtUtil;
import com.example.jwt.service.BlogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
@CrossOrigin(origins = "http://localhost:3000") // Add CORS support
public class BlogController {

    @Autowired
    private final BlogRepository blogRepository;
    private final BlogService blogService;
    private final JwtUtil jwtUtil;

    public BlogController(BlogService blogService, JwtUtil jwtUtil, BlogRepository blogRepository) {
        this.blogService = blogService;
        this.jwtUtil = jwtUtil;
        this.blogRepository = blogRepository;
    }

    // Save a new blog
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<BlogDTO>> saveBlog(
            @RequestBody Blog blog,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);
            Blog savedBlog = blogService.saveBlog(blog, email);

            return ResponseEntity.ok(new ApiResponse<>(true, "Blog saved successfully!", new BlogDTO(savedBlog)));
        } catch (Exception e) {
            System.err.println("Error saving blog: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(false, "Error saving blog: " + e.getMessage(), null));
        }
    }

    // Get all blogs for logged-in user
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<BlogDTO>>> getAllBlogs(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);

            List<BlogDTO> blogs = blogService.getAllBlogsByUser(email)
                                             .stream()
                                             .map(BlogDTO::new)
                                             .toList();
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Blogs fetched successfully", blogs));
        } catch (Exception e) {
            System.err.println("Error fetching blogs: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(false, "Error fetching blogs: " + e.getMessage(), null));
        }
    }

    @PutMapping("/update/{blogId}")
    public ResponseEntity<ApiResponse<BlogDTO>> updateBlog(
            @PathVariable Long blogId,
            @RequestBody Blog updatedBlog,
            @RequestHeader("Authorization") String authHeader) {

        try {
            System.out.println("=== UPDATE BLOG REQUEST ===");
            System.out.println("Blog ID: " + blogId);
            System.out.println("Request body - Title: " + updatedBlog.getBlogTitle());
            System.out.println("Request body - Content: " + updatedBlog.getBlogContent());
            System.out.println("Auth header: " + authHeader.substring(0, 20) + "...");
            
            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);
            System.out.println("Extracted email: " + email);

            Blog blog = blogService.updateBlog(blogId, updatedBlog, email);
            if (blog == null) {
                System.out.println("BlogService returned null - sending 400 response");
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Blog not found or unauthorized", null));
            }

            System.out.println("Blog updated successfully, creating response");
            BlogDTO responseDTO = new BlogDTO(blog);
            System.out.println("Response DTO created: " + responseDTO.getBlogTitle());
            
            ApiResponse<BlogDTO> response = new ApiResponse<>(true, "Blog updated successfully", responseDTO);
            System.out.println("=== UPDATE BLOG SUCCESS ===");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("=== UPDATE BLOG ERROR ===");
            System.err.println("Error updating blog: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(false, "Error updating blog: " + e.getMessage(), null));
        }
    }

    // Delete a blog
    @DeleteMapping("/delete/{blogId}")
    public ResponseEntity<ApiResponse<String>> deleteBlog(
            @PathVariable Long blogId,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);
            boolean deleted = blogService.deleteBlog(blogId, email);

            if (!deleted) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Blog not found or unauthorized", null));
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Blog deleted successfully", "Blog deleted"));
        } catch (Exception e) {
            System.err.println("Error deleting blog: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(false, "Error deleting blog: " + e.getMessage(), null));
        }
    }
}
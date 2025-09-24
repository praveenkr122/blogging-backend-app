package com.example.jwt.repository;

import com.example.jwt.entity.Blog;
import com.example.jwt.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

	// Correct method name for ManyToOne relation
    List<Blog> findByUser(User user);
}

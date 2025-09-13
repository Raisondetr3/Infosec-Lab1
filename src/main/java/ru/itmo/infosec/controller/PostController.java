package ru.itmo.infosec.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.infosec.dto.PostDto;
import ru.itmo.infosec.dto.PostsResponse;
import ru.itmo.infosec.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<PostsResponse> getAllPosts() {
        List<PostDto> posts = postService.getAllPosts();

        PostsResponse response = new PostsResponse(
                posts,
                posts.size(),
                "Posts retrieved successfully"
        );

        return ResponseEntity.ok(response);
    }
}

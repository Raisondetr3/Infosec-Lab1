package ru.itmo.infosec.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import ru.itmo.infosec.dto.PostDto;
import ru.itmo.infosec.entity.Post;
import ru.itmo.infosec.repo.PostRepository;
import ru.itmo.infosec.util.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
        try {
            List<Post> posts = postRepository.findAllPostsWithAuthor();

            return posts.stream()
                    .map(entityMapper::toPostDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch posts at the moment");
        }
    }
}

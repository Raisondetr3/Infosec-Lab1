package ru.itmo.infosec.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;
import ru.itmo.infosec.dto.LoginRequestDto;
import ru.itmo.infosec.dto.PostDto;
import ru.itmo.infosec.dto.UserDto;
import ru.itmo.infosec.entity.Post;
import ru.itmo.infosec.entity.User;

@Component
public class EntityMapper {

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername()
        );
    }

    public PostDto toPostDto(Post post) {
        if (post == null) {
            return null;
        }

        PostDto dto = new PostDto();
        dto.setId(post.getId());

        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());

        if (post.getAuthor() != null) {
            dto.setAuthorId(post.getAuthor().getId());
            dto.setAuthorUsername(post.getAuthor().getUsername());
        }

        return dto;
    }

    public User toUserEntity(LoginRequestDto dto, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUsername(HtmlUtils.htmlEscape(dto.getUsername()));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return user;
    }

    public Post toPostEntity(PostDto postDto) {
        if (postDto == null) {
            return null;
        }

        Post post = new Post();
        post.setId(postDto.getId());

        post.setTitle(postDto.getTitle() != null ? HtmlUtils.htmlEscape(postDto.getTitle()) : null);
        post.setContent(postDto.getContent() != null ? HtmlUtils.htmlEscape(postDto.getContent()) : null);

        return post;
    }
}
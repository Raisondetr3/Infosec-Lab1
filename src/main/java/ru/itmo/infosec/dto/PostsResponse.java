package ru.itmo.infosec.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostsResponse {
    private List<PostDto> posts;
    private int count;
    private String message;
}

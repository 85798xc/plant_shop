package com.example.plant_shop.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommentDTO {

    private final String text;
    private final Long parentId;
}
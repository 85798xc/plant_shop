package com.example.plant_shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class FileResponseDTO {

    private String name;
    private String uri;
    private String type;
    private long size;
}

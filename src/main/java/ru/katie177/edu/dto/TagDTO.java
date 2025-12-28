package ru.katie177.edu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    private Long id;
    private String name;
    private Integer usageCount;
}


package com.stackoverflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class QuestionRequestDTO {
    private Long Id;

    @NotBlank(message = "Title is required")
    @Size(min = 10, max = 255, message = "Title must be between 10 and 255 characters")
    private String title;

    @NotBlank(message = "description is required")
    @Size(min = 20, message = "description must contain at least 20 characters")
    private String description;

    private Set<String> tagsList;
    private Long bounties;
}
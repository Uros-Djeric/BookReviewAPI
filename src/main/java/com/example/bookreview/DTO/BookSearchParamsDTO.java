package com.example.bookreview.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookSearchParamsDTO {

    @Schema(description = "Filter by title (optional)")
    @Size(max = 100, message = "title too long")
    private String title;

    @Schema(description = "Filter by author (optional)")
    @Size(max = 100, message = "author too long")
    private String author;

    @Schema(description = "Define field for sorting author/title (optional)")
    @Pattern(regexp = "title|author", message = "sortField must be 'title' or 'author'")
    private String sortField;

    @Schema(description = "Define sorting order (asc/desc) (optional)")
    @Pattern(regexp = "asc|desc", message = "sortDir must be 'asc' or 'desc'")
    private String sortDir;
}

package com.example.bookreview.DTO;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

@Data
public class BookSearchParams {

    @Size(max = 100, message = "title too long")
    private String title;

    @Size(max = 100, message = "author too long")
    private String author;

    @Pattern(regexp = "title|author", message = "sortField must be 'title' or 'author'")
    private String sortField;

    @Pattern(regexp = "asc|desc", message = "sortDir must be 'asc' or 'desc'")
    private String sortDir;

    @NumberFormat
    private Integer page;

    @NumberFormat
    private Integer size;
}

package ru.bbpax.keeper.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bbpax.keeper.model.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {
    private String id;
    private String title;
    private LocalDateTime created;
    private String description;
    private List<Tag> tags;
}

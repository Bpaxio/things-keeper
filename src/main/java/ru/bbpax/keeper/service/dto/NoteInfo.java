package ru.bbpax.keeper.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.bbpax.keeper.model.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteInfo {
    @NonNull
    private String title;
    private LocalDateTime created;
    @NonNull
    private String description;
    private List<Tag> tags;
}

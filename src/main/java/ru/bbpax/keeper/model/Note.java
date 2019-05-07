package ru.bbpax.keeper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

import static ru.bbpax.keeper.model.NoteTypes.NOTE;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Document(collection = "notes")
@TypeAlias(NOTE)
@AllArgsConstructor
public class Note extends AbstractNote {

    public Note(String title, LocalDateTime created, String description, List<Tag> tags) {
        super(NOTE, title, created, description, tags);
    }
}

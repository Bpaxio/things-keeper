package ru.bbpax.keeper.model;


import com.querydsl.core.annotations.QueryEntity;
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
@QueryEntity
public class Note extends AbstractNote {

    public Note() {
        super(NOTE);
    }

    public Note(String title, LocalDateTime created, String description, List<Tag> tags) {
        super(title, created, description, tags, NOTE);
    }

    public Note(String id, String title, LocalDateTime created, String description, List<Tag> tags) {
        super(id, title, created, description, tags, NOTE);
    }
}

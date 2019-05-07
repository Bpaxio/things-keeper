package ru.bbpax.keeper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Document(collection = "notes")
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractNote {

    @Id
    @Field("_id")
    private String id;
    private String title;
    private LocalDateTime created;
    private String description;
    private String noteType;

    @DBRef
    private List<Tag> tags;

    public AbstractNote(String noteType, String title, LocalDateTime created, String description, List<Tag> tags) {
        this.title = title;
        this.created = created;
        this.description = description;
        this.noteType = noteType;
        this.tags = tags;
    }
}

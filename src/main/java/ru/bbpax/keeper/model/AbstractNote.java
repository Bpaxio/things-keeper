package ru.bbpax.keeper.model;


import com.querydsl.core.annotations.QueryEmbedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Document(collection = "notes")
@AllArgsConstructor
public abstract class AbstractNote {

    @Id
    @Field("_id")
    private String id;
    private String title;
    private LocalDateTime created;
    private String createdBy;
    private String description;
    @DBRef
    @QueryEmbedded
    private List<Tag> tags;
    private String noteType;

    public AbstractNote(String title, LocalDateTime created, String description, List<Tag> tags, String noteType) {
        this(noteType);
        this.title = title;
        this.created = created;
        this.description = description;
        this.tags = tags;
    }

    public AbstractNote(String noteType) {
        created = LocalDateTime.now();
        this.noteType = noteType;
    }
}

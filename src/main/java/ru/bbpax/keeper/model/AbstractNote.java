package ru.bbpax.keeper.model;


import com.querydsl.core.annotations.QueryEmbeddable;
import com.querydsl.core.annotations.QueryEmbedded;
import com.querydsl.core.annotations.QueryEntity;
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
@QueryEntity
@QueryEmbeddable
public abstract class AbstractNote {

    @Id
    @Field("_id")
    private String id;
    private String title;
    private LocalDateTime created;
    private String description;
    private String noteType;

    @DBRef
    @QueryEmbedded
    private List<Tag> tags;

    public AbstractNote(String noteType, String title, LocalDateTime created, String description, List<Tag> tags) {
        this(noteType);
        this.title = title;
        this.created = created;
        this.description = description;
        this.tags = tags;
    }

    public AbstractNote(String noteType) {
        this();
        this.noteType = noteType;
    }
    public AbstractNote() {
        created = LocalDateTime.now();
    }
}

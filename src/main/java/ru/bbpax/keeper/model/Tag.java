package ru.bbpax.keeper.model;

import com.querydsl.core.annotations.QueryEmbeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "tags")
@TypeAlias("Tag")
@NoArgsConstructor
@AllArgsConstructor
@QueryEmbeddable
public class Tag {
    @Id
    @Field("_id")
    private String id;
    @NonNull
    private String value;

    public Tag(String value) {
        this.value = value;
    }
}

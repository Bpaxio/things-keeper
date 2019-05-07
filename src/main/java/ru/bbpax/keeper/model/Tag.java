package ru.bbpax.keeper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document
@TypeAlias("Tag")
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @Field("_id")
    private String id;
    private String value;

    public Tag(String value) {
        this.value = value;
    }
}

package ru.bbpax.keeper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
public class Image {
    @Id
    @Field("_id")
    private String id;
    private String query;
    private String originalName;

    public Image(String query, String originalName) {
        this();
        this.query = query;
        this.originalName = originalName;
    }

    public Image() {
        this.id = new ObjectId().toHexString();
    }

}

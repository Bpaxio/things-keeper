package ru.bbpax.keeper.model;

import com.querydsl.core.annotations.QueryEmbeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@QueryEmbeddable
public class Image {
    @Id
    @Field("_id")
    private String id;
    private String link;
    private String originalName;

    public Image(String link, String originalName) {
        this();
        this.link = link;
        this.originalName = originalName;
    }

    public Image() {
        this.id = new ObjectId().toHexString();
    }

}

package ru.bbpax.keeper.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

import static ru.bbpax.keeper.model.NoteTypes.LINK_MARK;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Document(collection = "notes")
@TypeAlias(LINK_MARK)
@QueryEntity
public class LinkMark extends AbstractNote {
    private String link;

    public LinkMark(String title, LocalDateTime created, String description, List<Tag> tags, String link) {
        this(title, created, description, tags);
        this.link = link;
    }

    public LinkMark(String title, LocalDateTime created, String description, List<Tag> tags) {
        super(LINK_MARK, title, created, description, tags);
    }

    public LinkMark() {
        super(LINK_MARK);
    }
}

package ru.bbpax.keeper.rest.request;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper =  true)
@EqualsAndHashCode(callSuper = true)
public class NoteFilterRequest extends BaseFilterRequest {
    @ApiParam("Search 'input' in 'description' of the note")
    private boolean description;
}

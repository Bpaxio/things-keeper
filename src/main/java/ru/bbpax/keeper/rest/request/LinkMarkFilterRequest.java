package ru.bbpax.keeper.rest.request;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper =  true)
@EqualsAndHashCode(callSuper = true)
public class LinkMarkFilterRequest extends BaseFilterRequest {
    @ApiParam("Search 'input' in 'link' of the note")
    private boolean link;
    @ApiParam("Search 'input' in 'description' of the note")
    private boolean description;
}

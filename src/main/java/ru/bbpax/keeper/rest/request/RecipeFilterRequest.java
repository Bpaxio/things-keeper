package ru.bbpax.keeper.rest.request;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper =  true)
@EqualsAndHashCode(callSuper = true)
public class RecipeFilterRequest extends BaseFilterRequest {
    @ApiParam("Search 'input' in 'link' of the note")
    private boolean link;
    @ApiParam("Search 'input' in 'ingredient name' of the recipe(true by default)")
    private boolean ingredient;
    @ApiParam("'category' of the recipe")
    private String category;
    @ApiParam("Search by time of cooking")
    private String timeCooking;

    public RecipeFilterRequest() {
        ingredient = true;
    }
}

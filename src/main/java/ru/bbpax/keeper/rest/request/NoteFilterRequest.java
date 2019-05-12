package ru.bbpax.keeper.rest.request;

import lombok.Data;

@Data
public class NoteFilterRequest extends BaseFilterRequest {
    private String description;
}

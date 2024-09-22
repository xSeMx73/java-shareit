package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id;
    @Size(max = 100)
    String name;
    @Size(max = 500)
    String description;
    Boolean available;
    ItemRequest request;

}

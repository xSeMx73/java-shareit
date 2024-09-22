package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    Long id;
    @Size(max = 100)
    String name;
    @Size(max = 500)
    String description;
    Boolean available;
    User owner;
    ItemRequest request;
}

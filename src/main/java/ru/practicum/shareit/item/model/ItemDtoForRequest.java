package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ItemDtoForRequest {

    Long id;
    @Size(max = 200)
    String name;
    Long ownerId;

}
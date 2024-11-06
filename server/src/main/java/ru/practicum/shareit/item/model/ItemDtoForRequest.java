package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ItemDtoForRequest {

    Long id;
    String name;
    Long ownerId;

}
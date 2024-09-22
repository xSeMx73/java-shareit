package ru.practicum.shareit.user.model;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Long id;
    String name;
    String email;
}

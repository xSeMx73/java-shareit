package ru.practicum.shareit.request.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    Long id;
    @NotNull
    @Size(max = 500)
    String description;
    UserDto requester;
    @Builder.Default
    final LocalDateTime created = LocalDateTime.now();
}

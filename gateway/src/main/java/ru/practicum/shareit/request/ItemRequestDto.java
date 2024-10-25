package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    @NotBlank
    @Size(max = 1000)
    String description;
    @Builder.Default
    final LocalDateTime created = LocalDateTime.now();
}

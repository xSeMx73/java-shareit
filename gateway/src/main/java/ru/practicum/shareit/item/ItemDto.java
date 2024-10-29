package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    @NotBlank
    @Size(max = 150)
    String name;
    @NotBlank(message = "Описание не может быть пустым")
    @Size(max = 500)
    String description;
    Boolean available;
    @Positive
    Long requestId;
}

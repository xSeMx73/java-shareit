package ru.practicum.shareit.request.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.ItemDtoForRequest;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDtoForOwner {

    List<ItemDtoForRequest> items;
    Long id;
    @NotNull
    @Size(max = 500)
    String description;
    LocalDateTime created;

}

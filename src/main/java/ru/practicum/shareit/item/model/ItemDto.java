package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.comment.CommentDto;
import ru.practicum.shareit.request.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    Long id;
    @Size(max = 100)
    String name;
    @Size(max = 500)
    String description;
    LocalDateTime lastBooking;
    LocalDateTime nextBooking;
    Boolean available;
    ItemRequest request;
    List<CommentDto> comments;
}

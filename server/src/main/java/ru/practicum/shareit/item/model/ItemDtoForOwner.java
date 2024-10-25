package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.comment.CommentDto;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ItemDtoForOwner {
    String name;
    String description;
    LocalDateTime lastBooking;
    LocalDateTime nextBooking;
    List<CommentDto> comments;

}
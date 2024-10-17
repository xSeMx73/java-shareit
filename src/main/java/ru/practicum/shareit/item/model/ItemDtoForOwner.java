package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.Size;
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
    @Size(max = 200)
    String name;
    @Size(max = 400)
    String description;
    LocalDateTime lastBooking;
    LocalDateTime nextBooking;
    List<CommentDto> comments;

}
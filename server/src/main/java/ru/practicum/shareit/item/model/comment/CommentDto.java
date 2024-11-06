package ru.practicum.shareit.item.model.comment;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    Long id;
    String authorName;
    String text;
    @Builder.Default
   final LocalDateTime created = LocalDateTime.now();
}
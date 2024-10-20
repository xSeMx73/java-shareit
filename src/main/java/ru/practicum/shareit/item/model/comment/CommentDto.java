package ru.practicum.shareit.item.model.comment;

import jakarta.validation.constraints.Size;
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
    @Size(max = 500)
    String text;
    @Builder.Default
    LocalDateTime created = LocalDateTime.now();
}
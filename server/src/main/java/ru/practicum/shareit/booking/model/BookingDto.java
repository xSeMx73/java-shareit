package ru.practicum.shareit.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    Long id;
    Long itemId;
    LocalDateTime start;
    LocalDateTime end;
    ItemDto item;
    UserDto booker;
    @Builder.Default
    Booking.BookingStatus status = Booking.BookingStatus.WAITING;
}

package ru.practicum.shareit.booking.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.annotations.DateValidAnnotation;
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
    @NotNull(message = "Не указанна вещь")
    Long itemId;
    @DateValidAnnotation
    LocalDateTime start;
    @DateValidAnnotation
    LocalDateTime end;
    ItemDto item;
    UserDto booker;
    @Builder.Default
    Booking.BookingStatus status = Booking.BookingStatus.WAITING;

}

package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

   private Long id;
   private LocalDateTime start;
   private LocalDateTime end;
   private Item item;
   private User booker;
   private BookingStatus status;

    public enum BookingStatus {
        WAITING, APPROVED, REJECTED, CANCELED
    }

}



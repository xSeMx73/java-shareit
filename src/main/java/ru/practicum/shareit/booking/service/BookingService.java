package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(BookingDto bookingDto, Long userId);

    BookingDto approveBooking(Long bookingId, String approved, Long userId);

    BookingDto getBookingForId(Long bookingId, Long userId);

    List<BookingDto> getUserBookings(Booking.BookingState state, Long userId);

    List<BookingDto> getOwnerItemsBookings(Booking.BookingState state, Long userId);

}


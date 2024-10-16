package ru.practicum.shareit.booking.model;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking toBooking(BookingDto bookingDto);

    BookingDto toBookingDto(Booking booking);

}

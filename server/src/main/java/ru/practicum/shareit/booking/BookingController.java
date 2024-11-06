package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestBody BookingDto bookingDto,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        bookingDto = bookingService.createBooking(bookingDto, userId);
        log.info("Бронирования создано: {}", bookingDto);
        return bookingDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable("bookingId") Long bookingId,
                                     @RequestParam(name = "approved") Boolean approved,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        BookingDto bookingDto = bookingService.approveBooking(bookingId, approved, userId);
        log.info("Статус бронирования с ID : {} изменен", bookingId);
        return bookingDto;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingForId(@PathVariable("bookingId") Long bookingId,
                                      @RequestHeader("X-Sharer-User-Id") Long userId) {
        BookingDto bookingDto = bookingService.getBookingForId(bookingId, userId);
        log.info("Информация о бронировании получена");
        return bookingDto;
    }

    @GetMapping
    public List<BookingDto> getUserBookings(@RequestParam(name = "state", defaultValue = "ALL") Booking.BookingState state,
                                            @RequestHeader("X-Sharer-User-Id") Long userId) {
        List<BookingDto> bookingDtoList = bookingService.getUserBookings(state, userId);
        log.info("Запрос исполнен");
        return bookingDtoList;
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerItemsBookings(@RequestParam(name = "state", defaultValue = "ALL") Booking.BookingState state,
                                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        List<BookingDto> bookingDtoList = bookingService.getOwnerItemsBookings(state, userId);
        log.info("Запрос исполнен ");
        return bookingDtoList;
    }
}

package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> createBooking(@Valid @RequestBody BookingDto bookingDto,
												@RequestHeader("X-Sharer-User-Id") Long userId) {
		log.info("Создание бронирования : {}", bookingDto);
		return bookingClient.createBooking(userId, bookingDto);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approveBooking(@PathVariable("bookingId") Long bookingId,
												 @RequestParam(name = "approved") Boolean approved,
												 @RequestHeader("X-Sharer-User-Id") Long userId) {
		log.info("Изменение статуса бронирования с ID : {}", bookingId);
		return bookingClient.approveBooking(bookingId, approved, userId);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingForId(@RequestHeader("X-Sharer-User-Id") long userId,
												  @PathVariable Long bookingId) {
		log.info("Получение информации о бронировании с Id : {}", bookingId);
		return bookingClient.getBookingForId(userId, bookingId);
	}


	@GetMapping
	public ResponseEntity<Object> getUserBookings(@RequestHeader("X-Sharer-User-Id") long userId,
												  @RequestParam(name = "state", defaultValue = "all") String stateParam) {
		BookingDto.BookingState state = BookingDto.BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Неверный параметр бронирования: " + stateParam));
		log.info("Запрос бронирований пользователя с ID : {}", userId);
		return bookingClient.getUserBookings(userId, state);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getOwnerItemsBookings(@RequestParam(name = "state", defaultValue = "ALL") String stateParam,
												  @RequestHeader("X-Sharer-User-Id") Long userId) {
		BookingDto.BookingState state = BookingDto.BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Неверный параметр бронирования: " + stateParam));
		log.info("Запрос бронирований своих вещей пользователя с ID : {}", userId);
		return bookingClient.getOwnerItemsBookings(userId, state);

	}

}
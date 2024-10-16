package ru.practicum.shareit.booking.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ItemNotAvailableException;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

   private final BookingRepository bookingRepository;
   private final BookingMapper bookingMapper;
   private final UserService userService;
   private final ItemService itemService;


    @Override
    public BookingDto createBooking(BookingDto bookingDto, Long userId) {
     UserDto booker = userService.getUserById(userId);
      ItemDto itemDto = itemService.getItem(bookingDto.getItemId());
      if (!itemDto.getAvailable()) throw new ItemNotAvailableException("Вещь не доступна для бронирования");
      if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().equals(bookingDto.getEnd())) {
          throw new IllegalArgumentException("Неверно задано время бронирования");
}
        bookingDto.setBooker(booker);
        bookingDto.setItem(itemDto);
        bookingDto = bookingMapper.toBookingDto(bookingRepository.save(bookingMapper.toBooking(bookingDto)));
        bookingDto.setItemId(itemDto.getId());
        return bookingDto;
    }

    @Override
    public BookingDto approveBooking(Long bookingId, String approved, Long userId) {
    BookingDto bookingDto = getBookingForId(bookingId, userId);
    if (!Objects.equals(itemService.findOwnerIdByItemId(bookingDto.getItem().getId()), userId)) {
       throw new ValidationException("Попытка изменить статус брони не своей вещи");
    }
    if (approved.equals("true"))  {
        bookingDto.setStatus(Booking.BookingStatus.APPROVED);
    } else if (approved.equals("false")) {
        bookingDto.setStatus(Booking.BookingStatus.REJECTED);
        } else {
        throw new IllegalArgumentException("Ошибка подтверждения брони");
    }
    bookingRepository.save(bookingMapper.toBooking(bookingDto));
        return bookingDto;
    }

    @Override
    public BookingDto getBookingForId(Long bookingId, Long userId) {
        userService.getUserById(userId);
        BookingDto bookingDto = bookingMapper.toBookingDto(bookingRepository.findById(bookingId).orElseThrow(() -> new ItemNotAvailableException("Неверный ID бронирования")));
        if (!bookingDto.getBooker().getId().equals(userId) &&
            !itemService.findOwnerIdByItemId(bookingDto.getItem().getId()).equals(userId)) {
            throw new ValidationException("Информация по бронированию недоступна для данного пользователя");
        }
            return bookingDto;
    }

    @Override
    public List<BookingDto> getUserBookings(Booking.BookingState state, Long userId) {
        userService.getUserById(userId);
        List<Booking> userBookings = bookingRepository.findAllByBookerId(userId);
       return bookingFilter(userBookings, state);
    }

    @Override
    public List<BookingDto> getOwnerItemsBookings(Booking.BookingState state, Long userId) {
        userService.getUserById(userId);
        if (itemService.getUserItems(userId).isEmpty()) return new ArrayList<>();
        List<Booking> userBookings = bookingRepository.findAllBookersByItemOwnerId(userId);
        return bookingFilter(userBookings, state);
    }

    public List<BookingDto> bookingFilter(List<Booking> userBookings, Booking.BookingState state) {
        List<Booking> userBookingsTemp = userBookings;
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL : break;
            case CURRENT: userBookingsTemp = userBookings.stream()
                    .filter(b -> b.getStart().isBefore(now) && b.getEnd().isAfter(now))
                    .toList();
                break;
            case PAST: userBookingsTemp = userBookings.stream()
                    .filter(b -> b.getEnd().isBefore(now))
                    .toList();
                break;
            case FUTURE: userBookingsTemp = userBookings.stream()
                    .filter(b -> b.getStart().isAfter(now))
                    .toList();
                break;
            case WAITING: userBookingsTemp = userBookings.stream()
                    .filter(b -> b.getStatus().equals(Booking.BookingStatus.WAITING))
                    .toList();
                break;
            case REJECTED: userBookingsTemp = userBookings.stream()
                    .filter(b -> b.getStatus().equals(Booking.BookingStatus.REJECTED))
                    .toList();
                break;

        }
        return userBookingsTemp.stream()
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(bookingMapper::toBookingDto)
                .toList();
    }

}

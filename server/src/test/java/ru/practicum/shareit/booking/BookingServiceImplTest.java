package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.ItemNotAvailableException;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class BookingServiceImplTest {

    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    BookingService bookingService;

    ItemDto itemDto = ItemDto.builder().id(1L).name("Test name").description("Test description").available(true).build();
    ItemDto itemDto2 = ItemDto.builder().id(2L).name("Test name2").description("Test description2").available(false).build();
    UserDto userDto = new UserDto(1L, "Fedor", "fedor@mail.ru");
    UserDto userDto2 = new UserDto(2L, "Fedor2", "fedor2@mail.ru");
    BookingDto booking = BookingDto.builder().id(1L).itemId(itemDto.getId())
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusDays(1))
            .item(itemDto).booker(userDto).build();
    BookingDto booking1 = BookingDto.builder().id(2L).itemId(itemDto.getId())
            .start(LocalDateTime.now().plusDays(3))
            .end(LocalDateTime.now().plusDays(4))
            .item(itemDto).booker(userDto).build();
    BookingDto booking3 = BookingDto.builder().id(3L).itemId(itemDto2.getId())
            .start(LocalDateTime.now().plusDays(6))
            .end(LocalDateTime.now().plusDays(7))
            .item(itemDto).booker(userDto).build();
    BookingDto booking4 = BookingDto.builder().id(4L).itemId(itemDto.getId())
            .start(LocalDateTime.now().minusDays(1))
            .end(LocalDateTime.now().plusDays(1))
            .item(itemDto).booker(userDto).build();
    BookingDto booking5 = BookingDto.builder().id(5L).itemId(itemDto.getId())
            .start(LocalDateTime.now().minusDays(5))
            .end(LocalDateTime.now().minusDays(4))
            .item(itemDto).booker(userDto).build();


    @BeforeEach
    void setUp() {
        userDto = userService.createUserDto(userDto);
        itemDto = itemService.createItem(itemDto, userDto.getId());
    }


    @Test
    void createBooking() {
        BookingDto result = bookingService.createBooking(booking, userDto.getId());
        assertThat(result.getId()).isNotNull();
        assertEquals(result.getItemId(), booking.getItemId());
        assertEquals(result.getStart(), booking.getStart());
        assertEquals(result.getEnd(), booking.getEnd());
        assertEquals(result.getItem().getId(), booking.getItem().getId());
        assertEquals(result.getBooker(), booking.getBooker());
        assertEquals(result.getStatus(), booking.getStatus());
    }

    @Test
    void createBooking_notAvailableItem() {
        itemDto2 = itemService.createItem(itemDto2, userDto.getId());
        assertThatThrownBy(() -> bookingService.createBooking(booking3, userDto.getId()))
                .isInstanceOf(ItemNotAvailableException.class);
    }

    @Test
    void approveBooking() {
        BookingDto temp = bookingService.createBooking(booking, userDto.getId());
        BookingDto result = bookingService.approveBooking(temp.getId(), true, userDto.getId());
        assertThat(result.getId()).isNotNull();
        assertEquals(result.getStart(), temp.getStart());
        assertEquals(result.getEnd(), temp.getEnd());
        assertEquals(result.getItem().getId(), temp.getItem().getId());
        assertEquals(result.getBooker(), temp.getBooker());
        assertEquals(result.getStatus(), Booking.BookingStatus.APPROVED);
    }

    @Test
    void approveBooking_false() {
        BookingDto temp = bookingService.createBooking(booking, userDto.getId());
        BookingDto result = bookingService.approveBooking(temp.getId(), false, userDto.getId());
        assertThat(result.getId()).isNotNull();
        assertEquals(result.getStart(), temp.getStart());
        assertEquals(result.getEnd(), temp.getEnd());
        assertEquals(result.getItem().getId(), temp.getItem().getId());
        assertEquals(result.getBooker(), temp.getBooker());
        assertEquals(result.getStatus(), Booking.BookingStatus.REJECTED);
    }

    @Test
    void getBookingForId() {
        BookingDto temp = bookingService.createBooking(booking, userDto.getId());
        BookingDto result = bookingService.getBookingForId(temp.getId(), userDto.getId());
        result.setItemId(result.getItem().getId());
        assertEquals(temp, result);
    }

    @Test
    void getBookingForId_wrongId() {
        userDto2 = userService.createUserDto(userDto2);
        assertThatThrownBy(() -> bookingService.getBookingForId(1L, 2L))
                .isInstanceOf(ItemNotAvailableException.class);
    }

    @Test
    void getBookingForId_wrongUser() {
        userDto2 = userService.createUserDto(userDto2);
        bookingService.createBooking(booking, userDto.getId());
        assertThatThrownBy(() -> bookingService.getBookingForId(1L, 2L))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void getUserBookings() {
        BookingDto temp = bookingService.createBooking(booking, userDto.getId());
        BookingDto temp1 = bookingService.createBooking(booking1, userDto.getId());
        List<BookingDto> bookingList = bookingService.getUserBookings(Booking.BookingState.ALL, userDto.getId());
        bookingList.getFirst().setItemId(temp1.getItemId());
        bookingList.getLast().setItemId(temp.getItemId());
        assertEquals(bookingList.size(), 2);
        assertEquals(bookingList.getFirst(), temp1);
        assertEquals(bookingList.getLast(), temp);
    }

    @Test
    void getOwnerItemsBookings() {
        BookingDto temp = bookingService.createBooking(booking, userDto.getId());
        BookingDto temp1 = bookingService.createBooking(booking1, userDto.getId());
        List<BookingDto> bookingList = bookingService.getOwnerItemsBookings(Booking.BookingState.ALL, userDto.getId());
        bookingList.getFirst().setItemId(temp1.getItemId());
        bookingList.getLast().setItemId(temp.getItemId());
        assertEquals(bookingList.size(), 2);
        assertEquals(bookingList.getFirst(), temp1);
        assertEquals(bookingList.getLast(), temp);

    }

    @Test
    void getOwnerItemsBookings_stateCURRENT() {
        bookingService.createBooking(booking4, userDto.getId());
        List<BookingDto> bookingList = bookingService.getOwnerItemsBookings(Booking.BookingState.CURRENT, userDto.getId());
        assertEquals(bookingList.getFirst().getStart(), booking4.getStart());
        assertEquals(bookingList.getFirst().getEnd(), booking4.getEnd());
    }

    @Test
    void getOwnerItemsBookings_stateFUTURE() {
        bookingService.createBooking(booking1, userDto.getId());
        List<BookingDto> bookingList = bookingService.getOwnerItemsBookings(Booking.BookingState.FUTURE, userDto.getId());
        assertEquals(bookingList.getFirst().getStart(), booking1.getStart());
        assertEquals(bookingList.getFirst().getEnd(), booking1.getEnd());
    }

    @Test
    void getOwnerItemsBookings_statePAST() {
        bookingService.createBooking(booking5, userDto.getId());
        List<BookingDto> bookingList = bookingService.getOwnerItemsBookings(Booking.BookingState.PAST, userDto.getId());
        assertEquals(bookingList.getFirst().getStart(), booking5.getStart());
        assertEquals(bookingList.getFirst().getEnd(), booking5.getEnd());
    }

    @Test
    void getOwnerItemsBookings_WAITING() {
        booking4.setStatus(Booking.BookingStatus.WAITING);
        bookingService.createBooking(booking4, userDto.getId());
        List<BookingDto> bookingList = bookingService.getOwnerItemsBookings(Booking.BookingState.WAITING, userDto.getId());
        assertEquals(bookingList.getFirst().getStart(), booking4.getStart());
        assertEquals(bookingList.getFirst().getEnd(), booking4.getEnd());
    }

    @Test
    void getOwnerItemsBookings_REJECTED() {
        booking4.setStatus(Booking.BookingStatus.REJECTED);
        bookingService.createBooking(booking4, userDto.getId());
        List<BookingDto> bookingList = bookingService.getOwnerItemsBookings(Booking.BookingState.REJECTED, userDto.getId());
        assertEquals(bookingList.getFirst().getStart(), booking4.getStart());
        assertEquals(bookingList.getFirst().getEnd(), booking4.getEnd());
    }
}
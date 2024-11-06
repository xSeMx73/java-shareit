package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private BookingController bookingController;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void createBooking_shouldCreateBooking() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .itemId(1L)
                .build();

        when(bookingService.createBooking(any(BookingDto.class), eq(1L))).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.itemId").value(1));

        verify(bookingService, times(1)).createBooking(any(BookingDto.class), eq(1L));
    }

    @Test
    void approveBooking_shouldApproveBooking() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .status(Booking.BookingStatus.APPROVED)
                .build();

        when(bookingService.approveBooking(eq(1L), eq(true), eq(1L))).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("APPROVED"));

        verify(bookingService, times(1)).approveBooking(eq(1L), eq(true), eq(1L));
    }

    @Test
    void getBookingForId_shouldReturnBooking() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .build();

        when(bookingService.getBookingForId(eq(1L), eq(1L))).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(bookingService, times(1)).getBookingForId(eq(1L), eq(1L));
    }

    @Test
    void getUserBookings_shouldReturnUserBookings() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .build();

        when(bookingService.getUserBookings(any(), eq(1L))).thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(bookingService, times(1)).getUserBookings(any(), eq(1L));
    }

    @Test
    void getOwnerItemsBookings_shouldReturnOwnerBookings() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .build();

        when(bookingService.getOwnerItemsBookings(any(), eq(1L))).thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(bookingService, times(1)).getOwnerItemsBookings(any(), eq(1L));
    }
}
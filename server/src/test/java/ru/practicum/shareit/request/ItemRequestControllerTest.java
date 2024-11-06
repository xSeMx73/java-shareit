package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestDtoForOwner;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService requestService;

    @Autowired
    private ObjectMapper objectMapper;
    UserDto user;
    private ItemRequestDtoForOwner requestDtoForOwner;
    ItemRequestDto itemRequestDto;

    @BeforeEach
    public void setup() {

        user = UserDto.builder().build();
        user.setId(1L);
        user.setName("Fedor");
        user.setEmail("fedor@mail.ru");


        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Test description")
                .requester(user)
                .created(LocalDateTime.now())
                .build();



        requestDtoForOwner = ItemRequestDtoForOwner.builder()
                .id(1L)
                .description("Test description")
                .build();

    }

    @Test
    public void testCreateItemRequest() throws Exception {
        when(requestService.createRequest(itemRequestDto, 1L)).thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));

    }

    @Test
    public void getAllItemRequests() throws Exception {
        List<ItemRequestDto> requests = List.of(itemRequestDto);
        when(requestService.getAllRequests(user.getId())).thenReturn(requests);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription())));

    }

    @Test
    public void testGetItemRequest() throws Exception {
        when(requestService.getRequestById(requestDtoForOwner.getId(), user.getId()))
                .thenReturn(requestDtoForOwner);

        mockMvc.perform(get("/requests/" + requestDtoForOwner.getId())
                        .header("X-Sharer-User-Id", user.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDtoForOwner.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDtoForOwner.getDescription())));
    }

    @Test
    public void getUserItemRequests() throws Exception {
        List<ItemRequestDtoForOwner> requests = List.of(requestDtoForOwner);
        when(requestService.getUserItemRequests(user.getId())).thenReturn(requests);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(requests)));


    }
}
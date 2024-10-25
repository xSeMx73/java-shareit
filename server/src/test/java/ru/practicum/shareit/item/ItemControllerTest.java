package ru.practicum.shareit.item;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    ItemDto itemDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();

         itemDto = ItemDto.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .requestId(1L)
                .comments(Collections.emptyList())
                .build();
    }


    @Test
    void createItem_ShouldReturnItemDto() throws Exception {


        when(itemService.createItem(itemDto,1L)).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getItemById_ShouldReturnItemDto() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .lastBooking(LocalDateTime.now())
                .nextBooking(LocalDateTime.now().plusDays(1))
                .requestId(1L)
                .comments(Collections.emptyList())
                .build();

        when(itemService.getItem(1L)).thenReturn(itemDto);

        mockMvc.perform(get("/items/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Item"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    void updateItem_ShouldReturnUpdatedItemDto() throws Exception {
        ItemDto updatedItemDto = ItemDto.builder()
                .id(1L)
                .name("Updated Item")
                .description("Updated Description")
                .available(true)
                .requestId(1L)
                .comments(Collections.emptyList())
                .build();

        when(itemService.updateItem(updatedItemDto,1L,1L)).thenReturn(updatedItemDto);

        var response =  mockMvc.perform(
                patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedItemDto)))
                .andExpect(status().isOk());

        verify(itemService).updateItem(updatedItemDto,1L, 1L);
        assertThat(response.andReturn().getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(updatedItemDto));
    }


}
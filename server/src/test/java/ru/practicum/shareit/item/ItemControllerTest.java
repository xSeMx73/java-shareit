package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import ru.practicum.shareit.item.model.ItemDtoForOwner;
import ru.practicum.shareit.item.model.comment.CommentDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemController itemController;

    private final ObjectMapper objectMapper = new ObjectMapper();


    ItemDtoForOwner itemDtoForOwner;
    UserDto userDto = UserDto.builder().id(1L).name("Fedor").email("fedor@mail.ru").build();
    ItemDto itemDto = ItemDto.builder().id(1L).name("Test item").description("Test description").available(true)
                .requestId(1L)
                .comments(Collections.emptyList())
                .build();
    CommentDto commentDto = CommentDto.builder().text("Test text").authorName(userDto.getName()).build();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }


    @Test
    void createItem() throws Exception {


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
    void getItemById() throws Exception {
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
    void updateItem() throws Exception {
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

    @Test
    void getUserItems() throws Exception {

        itemDtoForOwner = ItemDtoForOwner.builder()
                .name("Name1")
                .description("Description")
                .comments(List.of())
                .build();

        List<ItemDtoForOwner> userItems = List.of(itemDtoForOwner);
        when(itemService.getUserItems(anyLong())).thenReturn(userItems);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostComment() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        when(itemService.createComment(commentDto,userDto.getId(),itemDto.getId()))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/" + itemDto.getId() + "/comment")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .content(objectMapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())));
    }

    }



package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestDtoForOwner;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class ItemRequestServiceImplTest {

    @Autowired
    ItemRequestService itemRequestService;

    @Autowired
    UserService userService;

    UserDto userDto = new UserDto(1L, "Fedor", "fedor@mail.ru");
    UserDto userDto2 = new UserDto(2L, "Symkin", "symkin@mail.ru");
    ItemRequestDto itemRequest = ItemRequestDto.builder().id(1L).description("Req description").requester(userDto).build();
    ItemRequestDto itemRequest2 = ItemRequestDto.builder().id(2L).description("Req description2").requester(userDto).build();
    ItemRequestDto itemRequest3 = ItemRequestDto.builder().id(3L).description("Req description3").requester(userDto2).build();


    @BeforeEach
    void setUp() {
        userDto = userService.createUserDto(userDto);
        userDto2 = userService.createUserDto(userDto2);
    }

    @Test
    void createRequest() {
        ItemRequestDto result = itemRequestService.createRequest(itemRequest, userDto.getId());
        assertThat(result.getId()).isNotNull();
        assertEquals(result.getDescription(), itemRequest.getDescription());
        assertEquals(result.getRequester(), itemRequest.getRequester());
        assertEquals(result.getCreated(), itemRequest.getCreated());
    }

    @Test
    void getUserItemRequests() {
        itemRequestService.createRequest(itemRequest, userDto.getId());
        itemRequestService.createRequest(itemRequest2, userDto.getId());
        List<ItemRequestDtoForOwner> itemRequestList = itemRequestService.getUserItemRequests(userDto.getId());
        assertEquals(itemRequestList.size(), 2);

    }

    @Test
    void getAllRequests() {
        itemRequestService.createRequest(itemRequest, userDto.getId());
        itemRequestService.createRequest(itemRequest2, userDto.getId());
        itemRequestService.createRequest(itemRequest3, userDto2.getId());
        List<ItemRequestDto> itemRequestList = itemRequestService.getAllRequests(userDto.getId());
        List<ItemRequestDto> itemRequestList2 = itemRequestService.getAllRequests(userDto2.getId());
        assertEquals(itemRequestList.size(), 1);
        assertEquals(itemRequestList2.size(), 2);

    }

    @Test
    void getRequestById() {
        itemRequest = itemRequestService.createRequest(itemRequest, userDto.getId());
        ItemRequestDtoForOwner result = itemRequestService.getRequestById(itemRequest.getId(), userDto.getId());
        assertThat(result.getId()).isNotNull();
        assertEquals(result.getDescription(), itemRequest.getDescription());
        assertEquals(result.getCreated(), itemRequest.getCreated());
    }

    @Test
    void getRequestByIdForCreateItem() {
        ItemRequestDto result = itemRequestService.createRequest(itemRequest, userDto.getId());
        ItemRequest request = itemRequestService.getRequestByIdForCreateItem(result.getId());
        assertThat(result.getId()).isNotNull();
        assertThat(request.getId()).isNotNull();
        assertEquals(result.getDescription(), request.getDescription());
        assertEquals(result.getCreated(), request.getCreated());
    }

    @Test
    void getRequestByIdForCreateItem_wrongId() {
        assertThatThrownBy(() -> itemRequestService.getRequestByIdForCreateItem(55L))
                .isInstanceOf(NotFoundException.class);

    }

}
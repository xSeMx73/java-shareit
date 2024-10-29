package ru.practicum.shareit.item;


import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.CollectionUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoForOwner;
import ru.practicum.shareit.item.model.comment.CommentDto;
import ru.practicum.shareit.item.model.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
class ItemServiceImplTest {

    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    BookingService bookingService;
    @Autowired
    ItemMapper mapper;


    UserDto userDto = new UserDto(1L, "Fedor", "fedor@mail.ru");
    ItemDto itemDto = ItemDto.builder().id(1L).name("Test name").description("Test description").available(true).build();
    ItemDto itemDto2 = ItemDto.builder().id(2L).name("Test name2").description("Test description2").requestId(1L).available(false).build();

    CommentDto comment = CommentDto.builder().id(1L).authorName(userDto.getName()).text("Test comment").build();

    @Test
    void createItem() {

        userDto = userService.createUserDto(userDto);
        ItemDto result = itemService.createItem(itemDto, userDto.getId());
        assertThat(result.getId(), notNullValue());
        assertEquals(result.getName(), itemDto.getName());
        assertEquals(result.getDescription(), itemDto.getDescription());
        assertEquals(result.getAvailable(), true);

    }

    @Test
    void updateItem() {
        userDto = userService.createUserDto(userDto);
        itemDto.setRequestId(null);
        itemDto2.setRequestId(null);
        ItemDto result = itemService.createItem(itemDto, userDto.getId());
        result = itemService.updateItem(itemDto2, result.getId(), userDto.getId());
        assertEquals(result.getName(), itemDto2.getName());
        assertEquals(result.getDescription(), itemDto2.getDescription());
        assertEquals(result.getAvailable(), itemDto2.getAvailable());

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void updateItem_wrongOwner() {
        userDto = userService.createUserDto(userDto);
        itemDto = itemService.createItem(itemDto, userDto.getId());
        assertThatThrownBy(() -> itemService.updateItem(itemDto2, itemDto.getId(), 80L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void updateItem_wrongUser() {
        assertThatThrownBy(() -> itemService.updateItem(itemDto2, itemDto.getId(), 80L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getItem() {
        userDto = userService.createUserDto(userDto);
        ItemDto result = itemService.createItem(itemDto, userDto.getId());
        ItemDto result1 = itemService.getItem(result.getId());
        assertEquals(result.getId(), result1.getId());
        assertEquals(result.getName(), result1.getName());
        assertEquals(result.getDescription(), result1.getDescription());
        assertEquals(result.getAvailable(), result1.getAvailable());
    }

    @Test
    void getUserItems() {
        userDto = userService.createUserDto(userDto);
        itemService.createItem(itemDto, userDto.getId());
        List<ItemDtoForOwner> items = itemService.getUserItems(userDto.getId());
        assertThat(CollectionUtils.isEmpty(items)).isFalse();
        ItemDtoForOwner itemDtoForOwner = items.getFirst();
        assertThat(itemDtoForOwner.getName()).isNotNull();
        assertThat(itemDtoForOwner.getDescription()).isNotNull();
    }


    @Test
    void findItemForText() {
        userDto = userService.createUserDto(userDto);
        ItemDto temp = itemService.createItem(itemDto, userDto.getId());
        List<ItemDto> items = itemService.findItemForText("Test description", userDto.getId());
        assertThat(CollectionUtils.isEmpty(items)).isFalse();
        ItemDto result = items.getFirst();
        assertEquals(temp, result);
    }

    @Test
    void findOwnerIdByItemId() {
        UserDto tempUser = userService.createUserDto(userDto);
        ItemDto tempItem = itemService.createItem(itemDto, tempUser.getId());
        Long ownerId = itemService.findOwnerIdByItemId(tempItem.getId());
        assertEquals(ownerId, tempUser.getId());
    }

    @Test
    void findOwnerIdByItemId_WrongItemId() {
        assertThatThrownBy(() -> itemService.findOwnerIdByItemId(55L))
                .isInstanceOf(NotFoundException.class);
    }


    @Test
    void createComment() {
        UserDto tempUser = userService.createUserDto(userDto);
        ItemDto tempItem = itemService.createItem(itemDto, tempUser.getId());

        BookingDto booking = BookingDto.builder().id(1L).itemId(tempItem.getId())
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(tempItem).booker(tempUser).status(Booking.BookingStatus.APPROVED).build();
        bookingService.createBooking(booking, tempUser.getId());

        CommentDto tempComment = itemService.createComment(comment, tempUser.getId(), tempItem.getId());
        assertThat(tempComment.getId()).isNotNull();
        assertEquals(tempComment.getText(), comment.getText());
        assertEquals(tempComment.getAuthorName(), comment.getAuthorName());
        assertThat(tempComment.getCreated()).isNotNull();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void mapperTest_toItem() {
        Item item = mapper.toItem(null);
        Assertions.assertThat(item).isNull();
    }

    @Test
    void mapperTest_toDTO() {
        ItemDto item = mapper.toItemDto(null);
        Assertions.assertThat(item).isNull();
    }

    @Test
    void mapperTest_toItemDtoForOwner() {
        ItemDtoForOwner item = mapper.toItemDtoForOwner(null);
        Assertions.assertThat(item).isNull();
    }


}
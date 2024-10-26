package ru.practicum.shareit.item.model.mapper;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDtoForRequest;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class ItemMapperDecoratorTest {

    @Autowired
    ItemMapper mapper;

    User user = User.builder().id(1L).name("Test user").build();
    Item item = Item.builder().id(1L).name("Test name").owner(user).build();


    @Test
    void toItemDtoForRequest() {
        ItemDtoForRequest result = mapper.toItemDtoForRequest(item);
        assertThat(result.getId(), notNullValue());
        assertEquals(result.getName(), item.getName());
        assertEquals(result.getOwnerId(), item.getOwner().getId());
    }
}
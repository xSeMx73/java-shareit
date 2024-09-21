package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoForOwner;
import ru.practicum.shareit.user.UserService;

@Component
@RequiredArgsConstructor
public class ItemMapper {

  private final UserService userService;


    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest() : null
        );
    }

        public Item toItem(ItemDto itemDto, Long ownerId) {
            return new Item(
                    itemDto.getId(),
                    itemDto.getName(),
                    itemDto.getDescription(),
                    itemDto.getAvailable(),
                    userService.getUserById(ownerId),
                    itemDto.getRequest() != null ? itemDto.getRequest() : null
            );
    }

    public ItemDtoForOwner toItemDtoForOwner(Item item) {
        return new ItemDtoForOwner(item.getName(), item.getDescription());
    }
}

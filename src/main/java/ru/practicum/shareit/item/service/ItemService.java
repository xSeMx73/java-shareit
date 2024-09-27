package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoForOwner;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId);

    ItemDto getItem(Long itemId);

    List<ItemDtoForOwner> getUserItems(Long userId);

    List<ItemDto> findItemForText(String text, Long userId);
}

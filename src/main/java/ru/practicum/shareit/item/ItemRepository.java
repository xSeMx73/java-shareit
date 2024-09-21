package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoForOwner;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemRepository {


 private final ItemMapper itemMapper;
  HashMap<Long, Item> items = new HashMap<>();


    public ItemDto createItem(ItemDto itemDto, Long userId) {
        Item item = itemMapper.toItem(itemDto, userId);
        items.put(item.getId(), item);
        return itemDto;
    }

    public ItemDto updateItem(ItemDto itemDto, Long itemId) {
        Item item = items.get(itemId);
       if (itemDto.getName() != null) item.setName(itemDto.getName());
       if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
       if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
       items.replace(itemId,item);
        return itemDto;
    }

    public ItemDto getItem(Long itemId) {
        return itemMapper.toItemDto(items.get(itemId));
    }

    public List<ItemDtoForOwner> getUserItems(Long userId) {
      return List.copyOf(items.values()).stream()
              .filter(item -> item.getOwner().getId().equals(userId))
              .map(itemMapper::toItemDtoForOwner)
              .toList();
    }

    public List<ItemDto> findItemForText(String text) {
        return List.copyOf(items.values()).stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                             || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .map(itemMapper::toItemDto)
                .toList();
    }
}

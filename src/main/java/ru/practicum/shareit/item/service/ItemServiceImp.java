package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoForOwner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImp implements ItemService {

   private Long id = 0L;
   private final ItemRepository itemRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
    if (itemDto.getName().isBlank() || itemDto.getDescription() == null || itemDto.getAvailable() == null) {
    throw new InternalException("Некорректно заполнены поля");
}
        itemDto.setId(++id);
        itemRepository.createItem(itemDto, userId);
        return itemDto;
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
        if (!itemRepository.getItems().containsKey(itemId)) {
            throw new NotFoundException("Вещи с таким id не существует");
        }
        if (!Objects.equals(itemRepository.getItems().get(itemId).getOwner().getId(), userId))
            throw new NotFoundException("Попытка обновить не свою вещь");
       itemDto.setId(itemId);
       itemDto = itemRepository.updateItem(itemDto, itemId);
        return itemDto;
    }

    @Override
    public ItemDto getItem(Long itemId) {
        ItemDto itemDto = itemRepository.getItem(itemId);
        if (itemDto == null) {
            throw new NotFoundException("Вещи с таким id не существует");
        }
        return itemDto;
    }

    @Override
    public List<ItemDtoForOwner> getUserItems(Long userId) {
     return itemRepository.getUserItems(userId);
    }

    @Override
    public List<ItemDto> findItemForText(String text,Long userId) {
        if (text.isBlank()) return new ArrayList<>();
        return itemRepository.findItemForText(text,userId);
    }
}

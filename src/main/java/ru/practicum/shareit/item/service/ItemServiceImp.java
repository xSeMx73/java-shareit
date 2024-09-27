package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoForOwner;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImp implements ItemService {


    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        if (itemDto.getName().isBlank() || itemDto.getDescription() == null || itemDto.getAvailable() == null) {
            throw new ValidationException("Некорректно заполнены поля");
        }
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(userMapper.toUser(userService.getUserById(userId)));
        itemRepository.createItem(item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
        getItem(itemId);
        if (!Objects.equals(itemRepository.getItems().get(itemId).getOwner().getId(), userId))
            throw new NotFoundException("Попытка обновить не свою вещь");
        itemDto.setId(itemId);
        itemDto = itemMapper.toItemDto(itemRepository.updateItem(itemMapper.toItem(itemDto), itemId));
        return itemDto;
    }

    @Override
    public ItemDto getItem(Long itemId) {
        ItemDto itemDto = itemMapper.toItemDto(itemRepository.getItem(itemId));
        if (itemDto == null) {
            throw new NotFoundException("Вещи с таким id не существует");
        }
        return itemDto;
    }

    @Override
    public List<ItemDtoForOwner> getUserItems(Long userId) {
        return itemRepository.getUserItems(userId).stream()
                .map(itemMapper::toItemDtoForOwner)
                .toList();
    }

    @Override
    public List<ItemDto> findItemForText(String text, Long userId) {
        userService.getUserById(userId);
        if (text.isBlank()) return new ArrayList<>();
        return itemRepository.findItemForText(text).stream()
                .map(itemMapper::toItemDto)
                .toList();
    }
}

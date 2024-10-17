package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoForOwner;
import ru.practicum.shareit.item.model.comment.CommentDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Добавление вещи - {}", itemDto);
        itemDto = itemService.createItem(itemDto, userId);
        log.info("Вещь добавлена - {}", itemDto);
        return itemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable("itemId") Long itemId) {
        log.info("Обновление вещи - {}", itemDto);
        itemDto = itemService.updateItem(itemDto, itemId, userId);
        log.info("Вещь обновлена - {}", itemDto);
        return itemDto;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable("itemId") Long itemId) {
        log.info("Запрос вещи с ID - {}", itemId);
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<ItemDtoForOwner> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Пользователь с ID - {} запросил список своих вещей", userId);
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemForText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(name = "text", required = false) String text) {
        log.info("Запрос вещи по описанию - {}", text);
        return itemService.findItemForText(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CommentDto commentDto, @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable("itemId") Long itemId) {
        log.info("Добавление комментария - {}, для вещи с ID : {}, от пользователя с ID : {}", commentDto, itemId, userId);
        commentDto = itemService.createComment(commentDto, userId, itemId);
        log.info("Комментарий добавлен для вещи с ID : {}", itemId);
        return commentDto;
    }

}

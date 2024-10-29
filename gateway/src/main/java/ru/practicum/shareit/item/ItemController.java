package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("Некорректно заполнены поля");
        }
        log.info("Добавление вещи - {}", itemDto);
        return itemClient.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable("itemId") Long itemId) {
        log.info("Обновление вещи - {}", itemDto);
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable("itemId") Long itemId) {
        log.info("Запрос вещи с ID - {}", itemId);
        return itemClient.getItem(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Пользователь с ID - {} запросил список своих вещей", userId);
        return itemClient.getUserItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemForText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(name = "text", required = false) String text) {
        log.info("Запрос вещи по описанию - {}", text);
        return itemClient.findItemForText(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestBody CommentDto commentDto, @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable("itemId") Long itemId) {
        log.info("Добавление комментария - {}, для вещи с ID : {}, от пользователя с ID : {}", commentDto, itemId, userId);
        return itemClient.createComment(commentDto, userId, itemId);
    }
}

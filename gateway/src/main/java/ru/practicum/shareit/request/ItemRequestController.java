package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;


    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Пользователь с ID : {} запросил вещь : {} ", userId, itemRequestDto);
        return itemRequestClient.createRequest(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Пользователь с ID : {} выводит список своих запросов", userId);
        return itemRequestClient.getUserItemRequests(userId);

    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Вывод списка всех запросов");
        return itemRequestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@PathVariable("requestId") Long requestId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Вывод запроса с ID: {}", requestId);
        return itemRequestClient.getRequestById(requestId, userId);
    }
}

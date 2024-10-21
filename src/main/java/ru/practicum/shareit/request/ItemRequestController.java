package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestDtoForOwner;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Пользователь с ID : {} запросил вещь : {} ", userId, itemRequestDto);
        ItemRequestDto requestDto = itemRequestService.createRequest(itemRequestDto, userId);
        log.info("Создан запрос вещи с ID : {}", requestDto.getId());
        return requestDto;
    }

    @GetMapping
    public List<ItemRequestDtoForOwner> getUserItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Пользователь с ID : {} выводит список своих запросов", userId);
        List<ItemRequestDtoForOwner> listRequestDto = itemRequestService.getUserItemRequest(userId);
        log.info("Запрос пользователя с ID : {} исполнен", userId);
        return listRequestDto;

    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Вывод списка всех запросов");
        return itemRequestService.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoForOwner getItemRequest(@PathVariable("requestId") Long requestId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Вывод запроса с ID: {}", requestId);
        return itemRequestService.getRequestById(requestId, userId);
    }
}

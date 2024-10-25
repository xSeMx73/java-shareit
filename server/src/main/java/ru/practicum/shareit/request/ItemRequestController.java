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
    public ItemRequestDto createItemRequest(@RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        ItemRequestDto requestDto = itemRequestService.createRequest(itemRequestDto, userId);
        log.info("Создан запрос вещи с ID : {}", requestDto.getId());
        return requestDto;
    }

    @GetMapping
    public List<ItemRequestDtoForOwner> getUserItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<ItemRequestDtoForOwner> listRequestDto = itemRequestService.getUserItemRequests(userId);
        log.info("Запрос пользователя с ID : {} исполнен", userId);
        return listRequestDto;

    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Вывод списка всех запросов");
        return itemRequestService.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoForOwner getItemRequest(@PathVariable("requestId") long requestId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Вывод запроса с ID: {}", requestId);
        return itemRequestService.getRequestById(requestId, userId);
    }
}

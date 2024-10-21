package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestDtoForOwner;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createRequest(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestDtoForOwner> getUserItemRequest(Long userId);

    List<ItemRequestDto> getAllRequests(Long userId);

    ItemRequestDtoForOwner getRequestById(Long requestId, Long userId);

    ItemRequest getRequestByIdForCreateItem(Long requestId);
}

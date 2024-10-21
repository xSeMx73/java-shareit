package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDtoForRequest;
import ru.practicum.shareit.item.model.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestDtoForOwner;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRepository itemRepository;


    private final ItemMapper itemMapper;


    @Override
    public ItemRequestDto createRequest(ItemRequestDto itemRequestDto, Long userId) {
        itemRequestDto.setRequester(userService.getUserById(userId));
        return itemRequestMapper.toItemRequestDto(itemRequestRepository
                .save(itemRequestMapper.toItemRequest(itemRequestDto)));
    }

    @Override
    public List<ItemRequestDtoForOwner> getUserItemRequest(Long userId) {
        userService.getUserById(userId);
        List<ItemRequest> userItemRequests = itemRequestRepository.findAllUserItemRequests(userId);
        if (userItemRequests.isEmpty()) throw new NotFoundException("Список запросов пуст");
        List<Long> requestsIds = userItemRequests.stream().map(ItemRequest::getId).toList();
        List<Item> items = itemRepository.findByRequests(requestsIds);
        List<ItemRequestDtoForOwner> returnRequests = new ArrayList<>();
        ItemRequestDtoForOwner tempRequest;
        for (ItemRequest request : userItemRequests) {
            List<ItemDtoForRequest> itemDtoForRequest = items.stream()
                    .filter(item -> item.getRequest().getId().equals(request.getId()))
                    .map(itemMapper::toItemDtoForRequest)
                    .toList();
            tempRequest = itemRequestMapper.toItemRequestDtoForOwner(request);
            tempRequest.setItems(itemDtoForRequest);
            returnRequests.add(tempRequest);
        }
        returnRequests.sort(Comparator.comparing(ItemRequestDtoForOwner::getCreated).reversed());
        return returnRequests;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId) {
        userService.getUserById(userId);
        return itemRequestRepository.findByRequesterIdNot(userId).stream()
                .map(itemRequestMapper::toItemRequestDto).toList();
    }

    @Override
    public ItemRequestDtoForOwner getRequestById(Long requestId, Long userId) {
        userService.getUserById(userId);
        ItemRequestDtoForOwner request = itemRequestMapper.toItemRequestDtoForOwner(itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден")));
        List<Item> items = itemRepository.findByRequestId(requestId);
        request.setItems(items.stream().map(itemMapper::toItemDtoForRequest).toList());

        return request;
    }

    @Override
    public ItemRequest getRequestByIdForCreateItem(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));
    }
}

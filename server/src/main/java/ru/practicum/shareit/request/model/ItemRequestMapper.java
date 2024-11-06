package ru.practicum.shareit.request.model;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    ItemRequest toItemRequest(ItemRequestDto itemRequestDto);

    ItemRequestDtoForOwner toItemRequestDtoForOwner(ItemRequest itemRequest);


}

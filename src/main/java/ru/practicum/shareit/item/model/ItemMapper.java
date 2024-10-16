package ru.practicum.shareit.item.model;

import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto toItemDto(Item item);

    Item toItem(ItemDto itemDto);

    ItemDtoForOwner toItemDtoForOwner(Item item);

}

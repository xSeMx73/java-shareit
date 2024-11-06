package ru.practicum.shareit.item.model.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoForOwner;
import ru.practicum.shareit.item.model.ItemDtoForRequest;

@Mapper(componentModel = "spring")
@DecoratedWith(ItemMapperDecorator.class)
public interface ItemMapper {

    ItemDto toItemDto(Item item);

    Item toItem(ItemDto itemDto);

    ItemDtoForOwner toItemDtoForOwner(Item item);

    ItemDtoForRequest toItemDtoForRequest(Item item);

}

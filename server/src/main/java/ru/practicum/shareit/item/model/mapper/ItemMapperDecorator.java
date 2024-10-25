package ru.practicum.shareit.item.model.mapper;

import org.mapstruct.DecoratedWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDtoForRequest;

@Component
@DecoratedWith(ItemMapper.class)
public abstract class ItemMapperDecorator implements ItemMapper {

    @Autowired
    @Qualifier("delegate")
    private ItemMapper delegate;

    @Override
    public ItemDtoForRequest toItemDtoForRequest(Item item) {

        ItemDtoForRequest itemDtoForRequest = delegate.toItemDtoForRequest(item);

        itemDtoForRequest.setId(item.getId());
        itemDtoForRequest.setName(item.getName());
        itemDtoForRequest.setOwnerId(item.ownerId());

        return itemDtoForRequest;
    }
    }



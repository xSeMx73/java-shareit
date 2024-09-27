package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemRepository {

    private Long id = 0L;
    @Getter
    HashMap<Long, Item> items = new HashMap<>();


    public Item createItem(Item item) {
        item.setId(++id);
        items.put(item.getId(), item);
        return item;
    }

    public Item updateItem(Item itemNew, Long itemId) {
        Item item = items.get(itemId);
        if (itemNew.getName() != null) item.setName(itemNew.getName());
        if (itemNew.getDescription() != null) item.setDescription(itemNew.getDescription());
        if (itemNew.getAvailable() != null) item.setAvailable(itemNew.getAvailable());
        items.replace(itemId, item);
        return item;
    }

    public Item getItem(Long itemId) {
        return items.get(itemId);
    }

    public List<Item> getUserItems(Long userId) {
        return List.copyOf(items.values()).stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .toList();
    }

    public List<Item> findItemForText(String text) {
        return List.copyOf(items.values()).stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                                || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .toList();
    }

}

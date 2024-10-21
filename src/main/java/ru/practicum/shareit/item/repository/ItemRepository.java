package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {


    List<Item> findItemsByOwnerId(Long ownerId);

    List<Item> findByDescriptionOrNameIgnoreCase(String description, String name);

    @Query (value = "select it.owner_id from items as it where id = ?1", nativeQuery = true)
    Optional<Long> findOwnerIdByItemId(Long itemId);

    @Query(value = "select * from items as i where REQUEST_ID in (:ids) ", nativeQuery = true)
    List<Item> findByRequests(@Param("ids") List<Long> ids);

    List<Item> findByRequestId(Long requestId);
}

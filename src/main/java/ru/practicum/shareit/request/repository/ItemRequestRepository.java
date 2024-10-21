package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query(value = "select * from requests as r where r.requester_id = ?1", nativeQuery = true)
    List<ItemRequest> findAllUserItemRequests(Long userId);

    List<ItemRequest> findByRequesterIdNot(Long userId);
}

package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select * from BOOKINGS as boo where boo.booker_id = ?1", nativeQuery = true)
    List<Booking> findAllByBookerId(Long userId);

    @Query(value = """
            select b.ID, START_DATE, END_DATE, ITEM_ID, BOOKER_ID, STATUS
            from bookings as b
            join public.items as it on b.item_id = it.id
            where it.owner_id = ?1""", nativeQuery = true)
    List<Booking> findAllBookersByItemOwnerId(Long userId);

    @Query(value = """
            select b.ID, START_DATE, END_DATE, ITEM_ID, BOOKER_ID, STATUS
            from bookings as b
            where  BOOKER_ID = ?1 and ITEM_ID = ?2""", nativeQuery = true)
    List<Booking> findAllByBookerIdAndItemId(Long userId, Long itemId);
}

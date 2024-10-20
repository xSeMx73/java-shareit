package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByItemId(Long id);

    @Query(value = """
            select com.id, text, item_id, author_id, created
            from comments as com
                join public.items it on it.id = com.item_id
                     where owner_id= ?1""", nativeQuery = true)
    List<Comment> findAllByItemOwnerId(Long userId);
}

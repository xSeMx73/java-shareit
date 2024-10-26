package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ItemNotAvailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoForOwner;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.comment.CommentDto;
import ru.practicum.shareit.item.model.comment.CommentMapper;
import ru.practicum.shareit.item.model.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ItemRequestService requestService;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        if (itemDto.getName().isBlank() || itemDto.getDescription() == null || itemDto.getAvailable() == null) {
            throw new ValidationException("Некорректно заполнены поля");
        }
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(userMapper.toUser(userService.getUserByIdForCreate(userId)));
        if (itemDto.getRequestId() != null) {
            item.setRequest(requestService.getRequestByIdForCreateItem(itemDto.getRequestId()));
        }
        itemDto = itemMapper.toItemDto(itemRepository.save(item));
        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getId());
        }
        return itemDto;
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещи с таким id не существует"));
        if (!Objects.equals(item.ownerId(), userId))
            throw new NotFoundException("Попытка обновить не свою вещь");
        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
        if (itemDto.getRequestId() != null) {
            item.setRequest(requestService.getRequestByIdForCreateItem(itemDto.getRequestId()));
        }
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItem(Long itemId) {
        ItemDto itemDto = itemMapper.toItemDto(itemRepository.findById(itemId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Вещи с id " + itemId + " не существует")));
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        itemDto.setComments(comments.stream().map(commentMapper::toCommentDto).toList());
        return itemDto;
    }

    @Override
    public List<ItemDtoForOwner> getUserItems(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookingsList = bookingRepository.findAllBookersByItemOwnerId(userId);
        List<Item> itemList = itemRepository.findItemsByOwnerId(userId);
        List<Comment> commentList = commentRepository.findAllByItemOwnerId(userId);
        Set<Long> itemIds = itemList.stream().map(Item::getId).collect(Collectors.toSet());
        List<ItemDtoForOwner> itemsForReturn = new ArrayList<>();
        List<CommentDto> commentListTemp = new ArrayList<>();
        ItemDtoForOwner tempItem;
        for (Item item : itemList) {
            BookingDto bookingLast = bookingsList.stream()
                    .filter(b -> itemIds.contains(b.getItem().getId()))
                    .filter(booking -> booking.getStart().isBefore(now))
                    .map(bookingMapper::toBookingDto)
                    .max(Comparator.comparing(BookingDto::getStart)).orElse(new BookingDto());

            BookingDto bookingUpcoming = bookingsList.stream()
                    .filter(b -> itemIds.contains(b.getItem().getId()))
                    .filter(booking -> booking.getStart().isAfter(now))
                    .map(bookingMapper::toBookingDto)
                    .min(Comparator.comparing(BookingDto::getStart)).orElse(new BookingDto());

            tempItem = itemMapper.toItemDtoForOwner(item);
            tempItem.setLastBooking(bookingLast.getEnd());
            tempItem.setNextBooking(bookingUpcoming.getStart());
            for (Comment c : commentList) {
                if (item.getId().equals(c.getItem().getId()))
                    commentListTemp.add(commentMapper.toCommentDto(c));
            }
            tempItem.setComments(commentListTemp);
            itemsForReturn.add(tempItem);
        }
        return itemsForReturn;
    }

    @Override
    public List<ItemDto> findItemForText(String text, Long userId) {
        userService.getUserById(userId);
        if (text.isBlank()) return new ArrayList<>();
        return itemRepository.findByDescriptionOrNameIgnoreCase(text, text).stream()
                .filter(item -> item.getAvailable().equals(true))
                .map(itemMapper::toItemDto)
                .toList();
    }

    @Override
    public Long findOwnerIdByItemId(Long itemId) {
        return itemRepository.findOwnerIdByItemId(itemId).orElseThrow(() ->
                new NotFoundException("Вещи с таким id не существует"));
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Long userId, Long itemId) {
        userService.getUserById(userId);
        getItem(itemId);
        List<Booking> userBookings = bookingRepository.findAllByBookerIdAndItemId(userId, itemId);
        userBookings.stream()
                .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                .findFirst().orElseThrow(() -> new ItemNotAvailableException("Комментирование вещи недоступно"));
        Comment comment = commentMapper.toComment(commentDto);
        comment.setAuthor(userMapper.toUser(userService.getUserByIdForCreate(userId)));
        comment.setItem(itemMapper.toItem(getItem(itemId)));
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }
}

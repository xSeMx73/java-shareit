package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   Long id;
   String description;
   @ManyToOne
   @JoinColumn(name = "requester_id")
   User requester;
   @Column(nullable = false, name = "created")
   LocalDateTime created;

}

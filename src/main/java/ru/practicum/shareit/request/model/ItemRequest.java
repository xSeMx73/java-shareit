package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
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

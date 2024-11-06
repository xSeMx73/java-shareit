package ru.practicum.shareit.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotBlank
    @Size(max = 150, message = "Слишком длинное имя")
    String name;
    @Size(max = 150, message = "Слишком длинный Email")
    @NotNull(message = "Email не может быть пустым")
    @Email(message = "Некорректный Email")
    String email;
}

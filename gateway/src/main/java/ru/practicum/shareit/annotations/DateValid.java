package ru.practicum.shareit.annotations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class DateValid implements ConstraintValidator<DateValidAnnotation, LocalDateTime> {


    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
            return value != null && !value.isBefore(LocalDateTime.now().plusSeconds(1));
    }
}
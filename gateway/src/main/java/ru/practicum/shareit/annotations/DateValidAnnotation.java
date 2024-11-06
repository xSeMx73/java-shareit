package ru.practicum.shareit.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { DateValid.class })
public @interface DateValidAnnotation {

    String message() default "Неверно указано время начала или конца бронирования";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

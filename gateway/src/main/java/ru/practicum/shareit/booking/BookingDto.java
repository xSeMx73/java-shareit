package ru.practicum.shareit.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
	@NotNull
	@Positive
	private long itemId;
	@FutureOrPresent
	private LocalDateTime start;
	@Future
	private LocalDateTime end;

	public enum BookingState {
		// Все
		ALL,
		// Текущие
		CURRENT,
		// Будущие
		FUTURE,
		// Завершенные
		PAST,
		// Отклоненные
		REJECTED,
		// Ожидающие подтверждения
		WAITING;

		public static Optional<BookingState> from(String stringState) {
			for (BookingState state : values()) {
				if (state.name().equalsIgnoreCase(stringState)) {
					return Optional.of(state);
				}
			}
			return Optional.empty();
		}
	}

}

package ru.bbpax.keeper.filter.core;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.bbpax.keeper.model.QAbstractNote.abstractNote;

@Data
public class DateIntervalFilter implements Filter {
    private final LocalDateTime from;
    private final LocalDateTime to;

    public DateIntervalFilter(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public DateIntervalFilter(LocalDate from, LocalDate to) {
        this.from = from != null ? from.atStartOfDay() : null;
        this.to = to != null ? to.atStartOfDay().plusDays(1) : null;
    }

    @Override
    public BooleanExpression toPredicate() {
        if (from != null && to != null) {
            return abstractNote.created.between(from, to);
        }
        if (from == null) {
            return abstractNote.created.before(to);
        }
        return abstractNote.created.goe(from);
    }

    @Override
    public Boolean isValid() {
        return !(from == null && to == null || fromAfterTo());
    }

    private boolean fromAfterTo() {
        return from != null && to != null && ChronoUnit.MILLIS.between(from, to) < 0;
    }
}

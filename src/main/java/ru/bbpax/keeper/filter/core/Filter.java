package ru.bbpax.keeper.filter.core;

import com.querydsl.core.types.dsl.BooleanExpression;

public interface Filter {

    BooleanExpression toPredicate();

    Boolean isValid();
}

package com.amatsuka.rememberer.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class QPredicatesBuilder {

    private final List<Predicate> predicates = new LinkedList<>();

    public static QPredicatesBuilder builder() {
        return new QPredicatesBuilder();
    }

    private QPredicatesBuilder() {}

    public <T> QPredicatesBuilder addIfNonNull(T nonNullObject, Function<T, Predicate> predicate) {
        if (Objects.nonNull(nonNullObject)) {
            this.predicates.add(predicate.apply(nonNullObject));
        }
        return this;
    }

    public Predicate build() {
        return ExpressionUtils.allOf(this.predicates);
    }
}

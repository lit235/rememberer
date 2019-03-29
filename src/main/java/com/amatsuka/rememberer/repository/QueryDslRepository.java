package com.amatsuka.rememberer.repository;

import com.amatsuka.rememberer.domain.entity.AbstractEntity;
import com.querydsl.core.types.dsl.EntityPathBase;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

@NoRepositoryBean
public interface QueryDslRepository<T extends AbstractEntity, P extends EntityPathBase<T>, ID extends Serializable>
        extends PagingAndSortingRepository<T, ID>, QuerydslPredicateExecutor<T>, QuerydslBinderCustomizer<P> {

    @Override
    default void customize(QuerydslBindings bindings, P root) {
    }
}

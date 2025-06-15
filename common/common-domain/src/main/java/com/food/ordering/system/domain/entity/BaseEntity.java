package com.food.ordering.system.domain.entity;


import java.util.Objects;

public abstract class BaseEntity<ID> {
    ID id;

    public ID id() {
        return id;
    }

    public void id(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

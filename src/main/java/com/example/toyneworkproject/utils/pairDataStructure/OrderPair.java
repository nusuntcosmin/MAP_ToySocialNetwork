package com.example.toyneworkproject.utils.pairDataStructure;

import java.util.Objects;

public class OrderPair<T1,T2> extends Pair<T1,T2>{

    public OrderPair(T1 firstElement, T2 secondElement) {
        super(firstElement, secondElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstElement(), getSecondElement());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() ) return false;
        OrderPair<?, ?> pair = (OrderPair<?, ?>) o;
        return Objects.equals(getFirstElement(), pair.getFirstElement()) && Objects.equals(getSecondElement(), pair.getSecondElement());
    }
}

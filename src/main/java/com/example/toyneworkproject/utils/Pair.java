package com.example.toyneworkproject.utils;

import java.util.Objects;

public class Pair<T1,T2> {
    T1 firstElement;
    T2 secondElement;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(firstElement, pair.firstElement) && Objects.equals(secondElement, pair.secondElement)
                ||
                Objects.equals(firstElement,pair.secondElement) && Objects.equals(secondElement,pair.firstElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstElement, secondElement);
    }

    public T1 getFirstElement() {
        return firstElement;
    }

    public void setFirstElement(T1 firstElement) {
        this.firstElement = firstElement;
    }

    public T2 getSecondElement() {
        return secondElement;
    }

    public void setSecondElement(T2 secondElement) {
        this.secondElement = secondElement;
    }

    public Pair(T1 firstElement, T2 secondElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }
}

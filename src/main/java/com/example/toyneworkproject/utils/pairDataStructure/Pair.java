package com.example.toyneworkproject.utils.pairDataStructure;

import java.util.Objects;

public class Pair<T1,T2>{
    private T1 firstElement;
    private T2 secondElement;
    public T1 getFirstElement() {
        return firstElement;
    }

    protected void setFirstElement(T1 firstElement) {
        this.firstElement = firstElement;
    }

    public T2 getSecondElement() {
        return secondElement;
    }

    protected void setSecondElement(T2 secondElement) {
        this.secondElement = secondElement;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(getFirstElement(), pair.getFirstElement()) && Objects.equals(getSecondElement(), pair.getSecondElement())
                ||
                Objects.equals(getFirstElement(),pair.getSecondElement()) && Objects.equals(getSecondElement(),pair.getFirstElement());
    }

    @Override
    public int hashCode() {
        return Math.abs(Objects.hash(getFirstElement(), getSecondElement()) - Objects.hash(getSecondElement(),getFirstElement()));

    }
    public Pair(T1 firstElement, T2 secondElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }
}

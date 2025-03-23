package com.app.swiftcodesproject.mappers;

public interface Mapper<A, B> {
    B mapToDto(A a);
    A mapToEntity(B b);
}

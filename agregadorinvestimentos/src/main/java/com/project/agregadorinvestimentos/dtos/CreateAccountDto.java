package com.project.agregadorinvestimentos.dtos;

public record CreateAccountDto(
        String description,
        String street,
        Integer number
) {
}

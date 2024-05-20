package com.project.agregadorinvestimentos.dtos;

public record AccountStockResponseDto(String stockId,
                                      int quantity,
                                      double total) {
}

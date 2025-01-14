package com.afcruz.securecapita.Dto;

public record PageableDTO(Integer page, Integer size, String sortBy, Object body) {
}

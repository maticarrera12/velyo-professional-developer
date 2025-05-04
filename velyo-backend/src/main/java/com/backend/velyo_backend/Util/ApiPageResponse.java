package com.backend.velyo_backend.Util;

public record ApiPageResponse<T>(int totalPages, int totalElements,T data, String message) {
}

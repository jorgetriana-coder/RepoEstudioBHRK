package com.bhrk.taskmanajemet.dto.error;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record BaseError(LocalDateTime timestamp, String message,int status) {
}

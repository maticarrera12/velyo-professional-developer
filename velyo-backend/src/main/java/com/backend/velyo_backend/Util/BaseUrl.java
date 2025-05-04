package com.backend.velyo_backend.Util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public interface BaseUrl {
    default String getBaseUrl(){
        return ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
    }
}

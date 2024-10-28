package com.delivery.api.domain.user.converter;

import com.delivery.api.common.annotation.Converter;
import com.delivery.api.common.error.ErrorCode;
import com.delivery.api.common.exception.ApiException;
import com.delivery.api.domain.user.controller.model.UserRegisterRequest;
import com.delivery.api.domain.user.controller.model.UserResponse;
import com.delivery.db.user.UserEntity;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
@Converter
public class UserConverter {

    public UserEntity toEntity(UserRegisterRequest request) {

        return Optional.ofNullable(request)
                .map(it -> {
                    // to Entity
                    return UserEntity.builder()
                            .name(it.getName())
                            .email(it.getEmail())
                            .password(it.getPassword())
                            .address(it.getAddress())
                            .build();

                })
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "UserRegisterRequest Null"));
    }

    public UserResponse toResponse(UserEntity userEntity) {
        return Optional.ofNullable(userEntity)
                .map(it -> {
                    // to Response
                    return UserResponse.builder()
                            .id(it.getId())
                            .name(it.getName())
                            .status(it.getStatus())
                            .email(it.getEmail())
                            .address(it.getAddress())
                            .registeredAt(it.getRegisteredAt())
                            .unregisteredAt(it.getUnregisteredAt())
                            .lastLoginAt(it.getLastLoginAt())
                            .build();
                })
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "UserEntity Null"));

    }
}

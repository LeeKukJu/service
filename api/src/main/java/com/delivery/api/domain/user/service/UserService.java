package com.delivery.api.domain.user.service;

import com.delivery.api.common.error.ErrorCode;
import com.delivery.api.common.error.UserErrorCode;
import com.delivery.api.common.exception.ApiException;
import com.delivery.db.user.UserEntity;
import com.delivery.db.user.UserRepository;
import com.delivery.db.user.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * User 도메인 로직을 처리하는 서비스
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserEntity register(UserEntity userEntity) {
        return Optional.ofNullable(userEntity)
                .map(it -> {
                    it.setStatus(UserStatus.REGISTERED);
                    it.setRegisteredAt(LocalDateTime.now());
                    return userRepository.save(it);
                })
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "UserEntity Null"));
    }

    public UserEntity login(
            String email,
            String password
    ) {
        var entity = getUserWithThrow(email, password);

        return entity;
    }

    public UserEntity getUserWithThrow(
        String email,
        String password
    ) {
        return userRepository.findByEmailAndPasswordAndStatusOrderByIdDesc(
                email,
                password,
                UserStatus.REGISTERED
        ).orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }
}

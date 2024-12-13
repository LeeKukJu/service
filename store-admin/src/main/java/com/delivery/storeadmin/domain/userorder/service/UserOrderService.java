package com.delivery.storeadmin.domain.userorder.service;

import com.delivery.db.userorder.UserOrderEntity;
import com.delivery.db.userorder.UserOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserOrderService {

    private final UserOrderRepository userOrderRepository;

    public Optional<UserOrderEntity> getUserOrder(Long userOrderId) {
        return userOrderRepository.findById(userOrderId);
    }
}

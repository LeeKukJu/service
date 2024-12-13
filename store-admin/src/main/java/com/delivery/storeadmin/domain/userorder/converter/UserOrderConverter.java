package com.delivery.storeadmin.domain.userorder.converter;

import com.delivery.db.userorder.UserOrderEntity;
import com.delivery.storeadmin.common.annotation.Converter;
import com.delivery.storeadmin.domain.userorder.controller.model.UserOrderResponse;

@Converter
public class UserOrderConverter {

    public UserOrderResponse toResponse(UserOrderEntity userOrderEntity) {
        return UserOrderResponse.builder()
            .id(userOrderEntity.getId())
            .storeId(userOrderEntity.getStoreId())
            .userId(userOrderEntity.getUserId())
            .status(userOrderEntity.getStatus())
            .amount(userOrderEntity.getAmount())
            .orderedAt(userOrderEntity.getOrderedAt())
            .acceptedAt(userOrderEntity.getAcceptedAt())
            .cookingStartedAt(userOrderEntity.getCookingStartedAt())
            .deliveryStartedAt(userOrderEntity.getDeliveryStartedAt())
            .receivedAt(userOrderEntity.getReceivedAt())
            .build();
    }
}

package com.delivery.storeadmin.domain.userorder.business;

import com.delivery.common.message.model.UserOrderMessage;
import com.delivery.storeadmin.common.annotation.Business;
import com.delivery.storeadmin.domain.sse.connection.SseConnectionPool;
import com.delivery.storeadmin.domain.storemenu.converter.StoreMenuConverter;
import com.delivery.storeadmin.domain.storemenu.service.StoreMenuService;
import com.delivery.storeadmin.domain.userorder.controller.model.UserOrderDetailResponse;
import com.delivery.storeadmin.domain.userorder.converter.UserOrderConverter;
import com.delivery.storeadmin.domain.userorder.service.UserOrderService;
import com.delivery.storeadmin.domain.userordermenu.service.UserOrderMenuService;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Business
public class UserOrderBusiness {

    private final UserOrderService userOrderService;
    private final UserOrderConverter userOrderConverter;

    private final SseConnectionPool sseConnectionPool;

    private final UserOrderMenuService userOrderMenuService;

    private final StoreMenuService storeMenuService;
    private final StoreMenuConverter storeMenuConverter;

    /**
     * 1. 주문
     * 2. 주문 내역 찾기
     * 3. 스토어 찾기
     * 4. 연결된 세션 찾기
     * 5. push
     */
    public void pushUserOrder(UserOrderMessage userOrderMessage) {

        var userOrderEntity = userOrderService.getUserOrder(userOrderMessage.getUserOrderId())
            .orElseThrow(() -> new RuntimeException("주문 내역을 찾을 수 없습니다."));

        var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenuList(userOrderEntity.getId());

        var storeMenuResponseList = userOrderMenuEntityList.stream()
            .map(userOrderMenuEntity -> {
                var storeMenu = storeMenuService.getStoreMenuWhitThrow(userOrderMenuEntity.getStoreMenuId());
                return storeMenu;
            })
            .map(storeMenuEntity -> {
                return storeMenuConverter.toResponse(storeMenuEntity);
            })
            .collect(Collectors.toList());

        var userOrderResponse = userOrderConverter.toResponse(userOrderEntity);

        var push = UserOrderDetailResponse.builder()
            .userOrderResponse(userOrderResponse)
            .storeMenuResponseList(storeMenuResponseList)
            .build();

        var userConnection = sseConnectionPool.getSession(userOrderEntity.getStoreId().toString());

        // 사용자에게 push
        userConnection.sendMessage(push);
    }
}

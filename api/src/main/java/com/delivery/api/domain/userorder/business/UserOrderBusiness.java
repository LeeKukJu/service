package com.delivery.api.domain.userorder.business;

import com.delivery.api.common.annotation.Business;
import com.delivery.api.domain.store.converter.StoreConverter;
import com.delivery.api.domain.store.service.StoreService;
import com.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import com.delivery.api.domain.storemenu.service.StoreMenuService;
import com.delivery.api.domain.user.model.User;
import com.delivery.api.domain.userorder.controller.model.UserOrderDetailResponse;
import com.delivery.api.domain.userorder.controller.model.UserOrderRequest;
import com.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import com.delivery.api.domain.userorder.converter.UserOrderConverter;
import com.delivery.api.domain.userorder.service.UserOrderService;
import com.delivery.api.domain.userordermenu.converter.UserOrderMenuConverter;
import com.delivery.api.domain.userordermenu.service.UserOrderMenuService;
import com.delivery.db.store.StoreEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Business
public class UserOrderBusiness {

  private final UserOrderService userOrderService;

  private final StoreMenuService storeMenuService;

  private final UserOrderConverter userOrderConverter;

  private final UserOrderMenuConverter userOrderMenuConverter;

  private final UserOrderMenuService userOrderMenuService;

  private final StoreService storeService;

  private final StoreMenuConverter storeMenuConverter;

  private final StoreConverter storeConverter;

  // 1. 사용자 , 메뉴 id
  // 2. userOrder 생성
  // 3. userOrderMenu 생성
  // 4. 응답 생성
  public UserOrderResponse userOrder(User user, UserOrderRequest body) {
    var storeMenuEntityList = body.getStoreMenuIdList()
        .stream()
        .map(storeMenuService::getStoreMenuWithThrow)
        .collect(Collectors.toList());

    var userOrderEntity = userOrderConverter.toEntity(user, storeMenuEntityList);

    // 주문
    var newUserOrderEntity = userOrderService.order(userOrderEntity);

    // 맵핑
    var userOrderMenuEntityList = storeMenuEntityList.stream()
        .map(it -> {
          // menu + user order
          var userOrderMenuEntity = userOrderMenuConverter.toEntity(newUserOrderEntity, it);
          return userOrderMenuEntity;
        })
        .collect(Collectors.toList());

    // 주문내역 기록 남기기
    userOrderMenuEntityList.forEach(it -> {
      userOrderMenuService.order(it);
    });

    // response
    return userOrderConverter.toResponse(newUserOrderEntity);
  }

  public List<UserOrderDetailResponse> current(User user) {

    var userOrderEntityList = userOrderService.current(user.getId());

    // 주문 1건씩 처리
    var userOrderDetailResponseList = userOrderEntityList.stream()
        .map(it -> {

          // 사용자가 주문한 메뉴 리스트
          var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getId());
          var storeMenuEntityList = userOrderMenuEntityList.stream()
              .map(userOrderMenuEntity -> {
                var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                return storeMenuEntity;
              })
              .collect(Collectors.toList());

          // 사용자가 주문한 스토어 TODO: 리팩토링 필요
          var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

          return UserOrderDetailResponse.builder()
              .userOrderResponse(userOrderConverter.toResponse(it))
              .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList))
              .storeResponse(storeConverter.toResponse(storeEntity))
              .build();
        })
        .collect(Collectors.toList());

    return userOrderDetailResponseList;
  }

  public List<UserOrderDetailResponse> history(User user) {
    var userOrderEntityList = userOrderService.history(user.getId());

    // 주문 1건씩 처리
    var userOrderDetailResponseList = userOrderEntityList.stream()
        .map(it -> {

          // 사용자가 주문한 메뉴 리스트
          var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getId());
          var storeMenuEntityList = userOrderMenuEntityList.stream()
              .map(userOrderMenuEntity -> {
                var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                return storeMenuEntity;
              })
              .collect(Collectors.toList());

          // 사용자가 주문한 스토어 TODO: 리팩토링 필요
          var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

          return UserOrderDetailResponse.builder()
              .userOrderResponse(userOrderConverter.toResponse(it))
              .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList))
              .storeResponse(storeConverter.toResponse(storeEntity))
              .build();
        })
        .collect(Collectors.toList());

    return userOrderDetailResponseList;
  }

  public UserOrderDetailResponse read(User user, Long orderId) {
    var userOrderEntity = userOrderService.getUserOrderWithOutStatusWithThrow(orderId, user.getId());

    // 사용자가 주문한 메뉴 리스트
    var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(userOrderEntity.getId());
    var storeMenuEntityList = userOrderMenuEntityList.stream()
        .map(userOrderMenuEntity -> {
          var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
          return storeMenuEntity;
        })
        .collect(Collectors.toList());

    // 사용자가 주문한 스토어 TODO: 리팩토링 필요
    var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

    return UserOrderDetailResponse.builder()
        .userOrderResponse(userOrderConverter.toResponse(userOrderEntity))
        .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList))
        .storeResponse(storeConverter.toResponse(storeEntity))
        .build();
  }
}

package com.delivery.storeadmin.domain.userorder.controller.model;

import com.delivery.storeadmin.domain.storemenu.controller.model.StoreMenuResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrderDetailResponse {

    private UserOrderResponse userOrderResponse;

    private List<StoreMenuResponse> storeMenuResponseList;
}

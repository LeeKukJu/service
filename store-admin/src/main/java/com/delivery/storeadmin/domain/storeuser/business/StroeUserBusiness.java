package com.delivery.storeadmin.domain.storeuser.business;

import com.delivery.db.store.StoreRepository;
import com.delivery.db.store.enums.StoreStatus;
import com.delivery.storeadmin.common.annotation.Business;
import com.delivery.storeadmin.domain.storeuser.controller.model.StoreUserRegisterRequest;
import com.delivery.storeadmin.domain.storeuser.controller.model.StoreUserResponse;
import com.delivery.storeadmin.domain.storeuser.converter.StoreUserConverter;
import com.delivery.storeadmin.domain.storeuser.service.StoreUserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Business
public class StroeUserBusiness {

    private final StoreUserConverter storeUserConverter;

    private final StoreUserService storeUserService;

    private final StoreRepository storeRepository;  // TODO SERVICE 로 변경하기

    public StoreUserResponse register(
        StoreUserRegisterRequest request
    ) {
        var storeEntity = storeRepository.findFirstByNameAndStatusOrderByIdDesc(request.getStoreName(), StoreStatus.REGISTERED);

        var entity = storeUserConverter.toEntity(request, storeEntity.get());

        var newEntity = storeUserService.register(entity);

        var response = storeUserConverter.toResponse(newEntity, storeEntity.get());

        return response;
    }
}

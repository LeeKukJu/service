package com.delivery.storeadmin.domain.storeuser.controller;

import com.delivery.storeadmin.domain.storeuser.business.StroeUserBusiness;
import com.delivery.storeadmin.domain.storeuser.controller.model.StoreUserRegisterRequest;
import com.delivery.storeadmin.domain.storeuser.controller.model.StoreUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/open-api/store-user")
public class StoreUserOpenApiController {

    private final StroeUserBusiness storeUserBusiness;

    @PostMapping("")
    public StoreUserResponse register(
            @Valid
            @RequestBody StoreUserRegisterRequest request
    ) {
        var response = storeUserBusiness.register(request);

        return response;
    }
}

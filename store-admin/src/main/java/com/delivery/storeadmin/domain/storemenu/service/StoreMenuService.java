package com.delivery.storeadmin.domain.storemenu.service;

import com.delivery.db.storemenu.StoreMenuEntity;
import com.delivery.db.storemenu.StoreMenuRepository;
import com.delivery.db.storemenu.enums.StoreMenuStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoreMenuService {

    private final StoreMenuRepository storeMenuRepository;

    public StoreMenuEntity getStoreMenuWhitThrow(Long id) {
        return storeMenuRepository.findFirstByIdAndStatusOrderByIdDesc(id, StoreMenuStatus.REGISTERED)
            .orElseThrow(() -> new RuntimeException("Store menu not found"));
    }
}

package com.delivery.storeadmin.domain.storemenu.converter;

import com.delivery.db.storemenu.StoreMenuEntity;
import com.delivery.storeadmin.common.annotation.Converter;
import com.delivery.storeadmin.domain.storemenu.controller.model.StoreMenuResponse;

import java.util.List;
import java.util.stream.Collectors;

@Converter
public class StoreMenuConverter {

    public StoreMenuResponse toResponse(StoreMenuEntity storeMenuEntity) {
        return StoreMenuResponse.builder()
            .id(storeMenuEntity.getId())
            .name(storeMenuEntity.getName())
            .amount(storeMenuEntity.getAmount())
            .status(storeMenuEntity.getStatus())
            .thumbnailUrl(storeMenuEntity.getThumbnailUrl())
            .likeCount(storeMenuEntity.getLikeCount())
            .sequence(storeMenuEntity.getSequence())
            .build();
    }

    public List<StoreMenuResponse> toResponse(List<StoreMenuEntity> list) {
        return list.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
}

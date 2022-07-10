package main_spring.mapper;

import main_spring.entity.ItemEntity;
import main_spring.web.response.ShopUnit;

public class ShopUnitMapper {
    public static ShopUnit convert(ItemEntity entity) {
        return ShopUnit.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType().toString())
                .date(entity.getIsoDate())
                .parentId(entity.getParentIdForJson())
                .price(entity.getPriceForJson())
                .children(entity.getChildrenForJson())
                .build();
    }
}

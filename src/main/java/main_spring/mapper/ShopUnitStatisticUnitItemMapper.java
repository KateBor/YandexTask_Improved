package main_spring.mapper;

import main_spring.entity.ItemEntity;
import main_spring.web.response.ShopUnitStatisticUnit;

import java.util.List;
import java.util.stream.Collectors;

public class ShopUnitStatisticUnitItemMapper {

    public static List<ShopUnitStatisticUnit> convert(List<ItemEntity> entities) {
        return entities.stream()
                .map(ShopUnitStatisticUnitItemMapper::convert)
                .collect(Collectors.toList());
    }

    public static ShopUnitStatisticUnit convert(ItemEntity entity) {
        return ShopUnitStatisticUnit.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType().toString())
                .date(entity.getIsoDate())
                .parentId(entity.getParentIdForJson())
                .price(entity.getPriceForJson())
                .build();
    }
}

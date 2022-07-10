package main_spring.mapper;

import main_spring.entity.HistoryEntity;
import main_spring.web.response.ShopUnitStatisticUnit;

import java.util.List;
import java.util.stream.Collectors;

public class ShopUnitStatisticUnitHistoryMapper {

    public static List<ShopUnitStatisticUnit> convert(List<HistoryEntity> entities) {
        return entities.stream()
                .map(ShopUnitStatisticUnitHistoryMapper::convert)
                .collect(Collectors.toList());
    }

    public static ShopUnitStatisticUnit convert(HistoryEntity entity) {
        return ShopUnitStatisticUnit.builder()
                .id(entity.getItemId())
                .name(entity.getName())
                .type(entity.getType().toString())
                .date(entity.getIsoDate())
                .parentId(entity.getParentId())
                .price(entity.getPrice())
                .build();
    }

}

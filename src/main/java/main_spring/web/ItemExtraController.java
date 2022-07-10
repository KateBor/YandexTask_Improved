package main_spring.web;

import main_spring.entity.HistoryEntity;
import main_spring.entity.ItemEntity;
import main_spring.mapper.ShopUnitStatisticUnitHistoryMapper;
import main_spring.mapper.ShopUnitStatisticUnitItemMapper;
import main_spring.service.HistoryService;
import main_spring.service.ItemService;
import main_spring.utils.Utils;
import main_spring.web.response.ShopUnitStatisticResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;

import static main_spring.utils.Utils.getValidationError;

@RestController
public class ItemExtraController {

    @Autowired
    ItemService itemService;

    @Autowired
    HistoryService historyService;

    @GetMapping(value = "/sales", produces = "application/json")
    public ResponseEntity getSales(@Param("date") String date) {
        if (date == null) {
            return getValidationError();
        }
        ZonedDateTime zonedDateTime = Utils.StringToZoneDateTime(date);
        if (zonedDateTime == null) {
            return getValidationError();
        }
        List<ItemEntity> sales = itemService.getUpdateItems(zonedDateTime);
        ShopUnitStatisticResponse response;
        if (!sales.isEmpty()) {
            response = ShopUnitStatisticResponse.builder()
                    .items(ShopUnitStatisticUnitItemMapper.convert(sales))
                    .build();
        } else {
            response = null;
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping(value = "/node/{id}/statistic", produces = "application/json")
    public ResponseEntity getStatisticItem(@PathVariable String id, @Param("dateStart") String dateStart, @Param("dateEnd") String dateEnd) {
        if (dateStart == null || dateEnd == null) {
            return getValidationError();
        }
        ZonedDateTime zonedDateTimeFrom = Utils.StringToZoneDateTime(dateStart);
        ZonedDateTime zonedDateTimeTo = Utils.StringToZoneDateTime(dateEnd);
        if (zonedDateTimeFrom == null || zonedDateTimeTo == null) {
            return getValidationError();
        }
        List<HistoryEntity> statistic = historyService.getStatistic(id, zonedDateTimeFrom, zonedDateTimeTo);

        ShopUnitStatisticResponse response = null;
        if (!statistic.isEmpty()) {
            response = ShopUnitStatisticResponse.builder()
                    .items(ShopUnitStatisticUnitHistoryMapper.convert(statistic))
                    .build();
        }
        return ResponseEntity.ok(response);
    }
}

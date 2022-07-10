package main_spring.web.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class ShopUnitStatisticResponse implements Serializable {
    List<ShopUnitStatisticUnit> items;
}

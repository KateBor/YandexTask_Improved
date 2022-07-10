package main_spring.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShopUnitImport implements Serializable {
    private ShopUnitType type;
    private String name;
    private String id;
    private String parentId;
    private Integer price;
}

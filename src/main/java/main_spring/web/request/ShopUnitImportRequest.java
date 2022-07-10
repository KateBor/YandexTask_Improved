package main_spring.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class ShopUnitImportRequest implements Serializable {
    private List<ShopUnitImport> items;
    private String updateDate;

    public String getUpdateDate() {
        return updateDate;
    }
}

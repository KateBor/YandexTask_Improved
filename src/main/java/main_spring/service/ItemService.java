package main_spring.service;

import main_spring.entity.ItemEntity;
import main_spring.web.request.ShopUnitImport;

import java.time.ZonedDateTime;
import java.util.List;

public interface ItemService {

    void importOffer(ShopUnitImport r, ZonedDateTime updateDate);

    void importCategory(ShopUnitImport r, ZonedDateTime updateDate);

    void deleteOffer(ItemEntity offer);

    void deleteCategory(ItemEntity category);

    List<ItemEntity> getUpdateItems(ZonedDateTime date);

    ItemEntity findById(String id);
}

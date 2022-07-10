package main_spring.web.validator;

import lombok.RequiredArgsConstructor;
import main_spring.entity.ItemEntity;
import main_spring.repository.ItemRepository;
import main_spring.web.request.ShopUnitImport;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static main_spring.web.request.ShopUnitType.CATEGORY;
import static main_spring.web.request.ShopUnitType.OFFER;

@Component
@RequiredArgsConstructor
public class ShopUnitImportValidator implements Validator {

    private final ItemRepository itemRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ShopUnitImport.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ShopUnitImport shopUnitImport = (ShopUnitImport) target;
        ValidationUtils.rejectIfEmpty(errors, "name", "empty_name");

        //родителем товара может быть только категория
        ItemEntity parent = null;
        if (shopUnitImport.getParentId() != null) {
            parent = itemRepository.findById(shopUnitImport.getParentId()).orElse(null);
        }
        if (parent != null && parent.getType() != CATEGORY) {
            errors.rejectValue("parentId", "invalid_type");
        }

        //цена
        if (shopUnitImport.getType().equals(OFFER) && (shopUnitImport.getPrice() == null || shopUnitImport.getPrice() <= 0)) {
            errors.rejectValue("price", "invalid_price");
        } else if (shopUnitImport.getType().equals(CATEGORY) && shopUnitImport.getPrice() != null) {
            errors.rejectValue("price", "invalid_price");
        }

        //смена типа не допускается
        ItemEntity existItem = itemRepository.findById(shopUnitImport.getId()).orElse(null);
        if (existItem != null && !existItem.getType().equals(shopUnitImport.getType())) {
            errors.rejectValue("type", "change_type");
        }
    }
}

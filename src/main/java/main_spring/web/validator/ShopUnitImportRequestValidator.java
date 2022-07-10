package main_spring.web.validator;

import lombok.RequiredArgsConstructor;
import main_spring.entity.ItemEntity;
import main_spring.repository.ItemRepository;
import main_spring.web.request.ShopUnitImport;
import main_spring.web.request.ShopUnitImportRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static main_spring.web.request.ShopUnitType.CATEGORY;
import static main_spring.web.request.ShopUnitType.OFFER;

@Component
@RequiredArgsConstructor
public class ShopUnitImportRequestValidator implements Validator {

    private final ItemRepository itemRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ShopUnitImportRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ShopUnitImportRequest shopUnitImportRequest = (ShopUnitImportRequest) target;
        ValidationUtils.rejectIfEmpty(errors, "updateDate", "empty_date");
        for (int i = 0; i < shopUnitImportRequest.getItems().size(); i++ ) {
            ShopUnitImport r = shopUnitImportRequest.getItems().get(i);
            ValidationUtils.rejectIfEmpty(errors, "items["+ i + "].name", "empty_name");

            //родителем товара может быть только категория
            ItemEntity parent = null;
            if (r.getParentId() != null) {
                parent = itemRepository.findById(r.getParentId()).orElse(null);
            }
            if (parent != null && parent.getType() != CATEGORY) {
                errors.rejectValue("items["+ i + "].parentId", "invalid_type");
            }

            //цена
            if (r.getType().equals(OFFER) && (r.getPrice() == null || r.getPrice() <= 0)) {
                errors.rejectValue("items["+ i + "].price", "invalid_price");
            } else if (r.getType().equals(CATEGORY) && r.getPrice() != null) {
                errors.rejectValue("items["+ i + "].price", "invalid_price");
            }

            //смена типа не допускается
            ItemEntity existItem = itemRepository.findById(r.getId()).orElse(null);
            if (existItem != null && !existItem.getType().equals(r.getType())) {
                errors.rejectValue("items["+ i + "].type", "change_type");
            }
        }
    }
}

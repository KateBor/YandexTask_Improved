package main_spring.web;

import main_spring.entity.ItemEntity;
import main_spring.mapper.ShopUnitMapper;
import main_spring.service.ItemService;
import main_spring.utils.Utils;
import main_spring.web.request.ShopUnitImport;
import main_spring.web.request.ShopUnitImportRequest;
import main_spring.web.validator.ShopUnitImportRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

import static main_spring.utils.Utils.getNotFoundError;
import static main_spring.utils.Utils.getValidationError;

@RestController
public class ItemBasicController {

    @Autowired
    ItemService itemService;

    @Autowired
    ShopUnitImportRequestValidator requestValidator;

    @PostMapping(value = "/imports", consumes = "application/json", produces = "application/json")
    public ResponseEntity importItems(@RequestBody ShopUnitImportRequest req, BindingResult bindingResult) {
        requestValidator.validate(req, bindingResult);
        if (bindingResult.hasErrors()) {
            return getValidationError();
        }
        ZonedDateTime date = Utils.StringToZoneDateTime(req.getUpdateDate());

        for (ShopUnitImport r : req.getItems()) {
            UUID.fromString(r.getId()); //проверка
            switch (r.getType()) {
                case OFFER:
                    itemService.importOffer(r, date);
                    break;
                case CATEGORY:
                    itemService.importCategory(r, date);
                    break;
            }
        }
        return ResponseEntity.ok().build();
    }


    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity deleteItem(@PathVariable String id) {
        UUID.fromString(id); //проверка
        ItemEntity item = itemService.findById(id);
        if (item == null) {
            return Utils.getNotFoundError(); //404
        }
        switch (item.getType()) {
            case OFFER:
                itemService.deleteOffer(item);
                break;
            case CATEGORY:
                itemService.deleteCategory(item);
                break;
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(value = "/nodes/{id}", produces = "application/json")
    public ResponseEntity getItem(@PathVariable String id) {
        UUID.fromString(id); //проверка
        ItemEntity item = itemService.findById(id);
        if (item != null) {
            return ResponseEntity.ok(ShopUnitMapper.convert(item));
        } else {
            return getNotFoundError();
        }
    }


    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        System.out.println("hello");
        return "hello!";
    }
}

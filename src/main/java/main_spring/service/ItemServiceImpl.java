package main_spring.service;

import lombok.RequiredArgsConstructor;
import main_spring.entity.HistoryEntity;
import main_spring.entity.ItemEntity;
import main_spring.repository.HistoryRepository;
import main_spring.repository.ItemRepository;
import main_spring.web.request.ShopUnitImport;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static main_spring.web.request.ShopUnitType.CATEGORY;
import static main_spring.web.request.ShopUnitType.OFFER;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final HistoryRepository historyRepository;

    @Override
    public void importOffer(ShopUnitImport r, ZonedDateTime updateDate) {
        String id = r.getId();
        String parentId = r.getParentId();
        ItemEntity parent = null;
        if (parentId != null) {
            parent = itemRepository.findById(parentId).orElse(null);
        }

        //обновление цены
        Integer priceBefore = 0;
        Integer priceAfter = r.getPrice();
        ItemEntity existItem = itemRepository.findById(id).orElse(null);
        if (existItem != null) {
            String oldParentId = null;
            if (existItem.getParentId() != null) {
                oldParentId = existItem.getParentId().getId();
                priceBefore = existItem.getPrice();
            }

            if (!priceBefore.equals(priceAfter) && Objects.equals(oldParentId, parentId)) {
                while (parentId != null && (parent = itemRepository.findById(parentId).orElse(null)) != null) {
                    itemRepository.updateOfferPrice(parentId, priceBefore, priceAfter, updateDate);
                    historyRepository.save(new HistoryEntity(parent));
                    if (parent.getParentId() != null) {
                        parentId = parent.getParentId().getId();
                    } else {
                        break;
                    }
                }
            } else if (!Objects.equals(oldParentId, parentId)) {

                while (parentId != null && (parent = itemRepository.findById(parentId).orElse(null)) != null) { // новый родитель
                    itemRepository.addOfferPrice(parentId, priceAfter, updateDate);
                    itemRepository.addChildren(parentId, 1); //плюс один ребенок
                    historyRepository.save(new HistoryEntity(itemRepository.findById(parentId).get()));
                    if (parent.getParentId() != null) {
                        parentId = parent.getParentId().getId();
                    } else {
                        break;
                    }
                }
                ItemEntity oldParent;
                while (oldParentId != null && (oldParent = itemRepository.findById(oldParentId).orElse(null)) != null) { // предыдущий родитель
                    itemRepository.subOfferPrice(oldParentId, priceBefore);
                    itemRepository.subChildren(oldParentId, 1); //минус один ребенок

                    if (oldParent.getParentId() != null) {
                        oldParentId = oldParent.getParentId().getId();
                    } else {
                        break;
                    }
                }
                itemRepository.updateOffer(existItem.getId(), r.getName(), r.getPrice(), parent, updateDate);
                historyRepository.save(new HistoryEntity(itemRepository.findById(existItem.getId()).get()));
            } else {
                //просто обновить дату
                while (parentId != null && (parent = itemRepository.findById(parentId).orElse(null)) != null) {
                    itemRepository.updateDate(parentId, updateDate);
                    historyRepository.save(new HistoryEntity(itemRepository.findById(parentId).get()));
                    if (parent.getParentId() != null) {
                        parentId = parent.getParentId().getId();
                    } else {
                        break;
                    }
                }
            }
        } else {
            ItemEntity offer = new ItemEntity(OFFER, r.getId(), r.getName(), r.getPrice(), parent, updateDate);
            itemRepository.save(offer);
            historyRepository.save(new HistoryEntity(offer));
            while (parentId != null && (parent = itemRepository.findById(parentId).orElse(null)) != null) { // новый родитель
                itemRepository.addOfferPrice(parentId, priceAfter, updateDate);
                itemRepository.addChildren(parentId, 1); //плюс один ребенок
                if (parent.getParentId() != null) {
                    parentId = parent.getParentId().getId();
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public void importCategory(ShopUnitImport r, ZonedDateTime updateDate) {
        String id = r.getId();
        String parentId = r.getParentId();

        ItemEntity parent = null;
        if (r.getParentId() != null) {
            Optional<ItemEntity> optionalItem = itemRepository.findById(r.getParentId());
            if (optionalItem.isPresent()) {
                parent = optionalItem.get();
            }
        }
        ItemEntity item;
        if ((item = itemRepository.findById(id).orElse(null)) != null) {
            //обновнение цены категории при смене родителя
            String oldParentId = null;
            if (item.getParentId() != null) {
                oldParentId = item.getParentId().getId();
            }
            if (!Objects.equals(parentId, oldParentId)) {
                while (parentId != null && (parent = itemRepository.findById(parentId).orElse(null)) != null) { // новый родитель
                    itemRepository.addCategoryPrice(parentId, item.getPrice(), item.getNumberOfChildren(), updateDate);
                    historyRepository.save(new HistoryEntity(itemRepository.findById(parentId).get()));
                    itemRepository.addChildren(parentId, item.getNumberOfChildren()); // плюс дети
                    if (parent.getParentId() != null) {
                        parentId = parent.getParentId().getId();
                    } else {
                        break;
                    }
                }
                ItemEntity oldParent;
                while (oldParentId != null && (oldParent = itemRepository.findById(oldParentId).orElse(null)) != null) { // предыдущий родитель
                    itemRepository.subCategoryPrice(oldParentId, item.getPrice(), item.getNumberOfChildren());
                    itemRepository.subChildren(parentId, item.getNumberOfChildren());//минус дети
                    if (oldParent.getParentId() != null) {
                        oldParentId = oldParent.getParentId().getId();
                    } else {
                        break;
                    }
                }
            }
            itemRepository.updateCategory(r.getId(), r.getName(), parent, updateDate);
            historyRepository.save(new HistoryEntity(itemRepository.findById(r.getId()).get()));
        } else {
            ItemEntity category = new ItemEntity(CATEGORY, r.getId(), r.getName(), 0, parent, updateDate);
            itemRepository.save(category);
            historyRepository.save(new HistoryEntity(category));
        }
    }

    @Override
    public void deleteOffer(ItemEntity offer) {
        String parentId = null;
        if (offer.getParentId() != null) {
            parentId = offer.getParentId().getId();
        }
        while (parentId != null) {
            itemRepository.subOfferPrice(parentId, offer.getPrice());
            itemRepository.subChildren(parentId, 1);
            ItemEntity item = itemRepository.findById(parentId).orElse(null);
            if (item != null && item.getParentId() != null) {
                parentId = item.getParentId().getId();
            } else {
                break;
            }
        }
        historyRepository.deleteItemsById(offer.getId());
        itemRepository.delete(offer);
    }

    @Override
    public void deleteCategory(ItemEntity category) {
        String parentId = null;
        if (category.getParentId() != null) {
            parentId = category.getParentId().getId();
        }
        while (parentId != null) {
            itemRepository.subCategoryPrice(parentId, category.getPrice(), category.getNumberOfChildren());
            itemRepository.subChildren(parentId, category.getNumberOfChildren());
            ItemEntity item = itemRepository.findById(parentId).orElse(null);
            if (item!= null && item.getParentId() != null) {
                parentId = item.getParentId().getId();
            } else {
                break;
            }
        }
        historyRepository.deleteItemsById(category.getId());
        itemRepository.delete(category);
    }

    @Override
    public List<ItemEntity> getUpdateItems(ZonedDateTime date) {
        ZonedDateTime after = date.minusHours(24);
        return itemRepository.getSales(after, date);
    }

    @Override
    public ItemEntity findById(String id) {
        Optional<ItemEntity> optionalItem = itemRepository.findById(id);
        return optionalItem.orElse(null);
    }
}

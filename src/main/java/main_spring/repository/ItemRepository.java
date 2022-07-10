package main_spring.repository;

import main_spring.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;


@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, String> {

    @Transactional
    @Modifying
    @Query("update ItemEntity i set i.price = (i.price * i.numberOfChildren - ?2 + ?3)/i.numberOfChildren, i.date = ?4 where i.id = ?1")
    void updateOfferPrice(String parentId, Integer priceBefore, Integer priceAfter, ZonedDateTime updateDate);

    @Transactional
    @Modifying
    @Query("update ItemEntity i set i.price = (i.price * i.numberOfChildren + ?2)/(i.numberOfChildren + 1), i.date = ?3 where i.id = ?1")
    void addOfferPrice(String parentId, Integer priceAfter, ZonedDateTime updateDate);

    @Transactional
    @Modifying
    @Query("update ItemEntity i set i.price = (i.price * i.numberOfChildren - ?2)/(i.numberOfChildren - 1) where i.id = ?1")
    void subOfferPrice(String parentId, Integer priceBefore);

    @Transactional
    @Modifying
    @Query("update ItemEntity i set i.name = ?2, i.price = ?3, i.parentId = ?4, i.date = ?5 where i.id = ?1")
    void updateOffer(String id, String name, Integer price, ItemEntity parentId, ZonedDateTime updateDate);

    @Transactional
    @Modifying
    @Query("update ItemEntity i set i.name = ?2, i.parentId = ?3, i.date = ?4 where i.id = ?1")
    void updateCategory(String id, String name, ItemEntity parentId, ZonedDateTime updateDate);

    @Transactional
    @Modifying
    @Query("update ItemEntity i set i.price = (i.price * i.numberOfChildren + ?2 * ?3)/(i.numberOfChildren + ?3), i.date = ?4 where i.id = ?1")
    void addCategoryPrice(String parentId, Integer price, Integer numberOfChildren, ZonedDateTime updateDate);

    @Transactional
    @Modifying
    @Query("update ItemEntity i set i.price = (i.price * i.numberOfChildren - ?2 * ?3)/(i.numberOfChildren - ?3) where i.id = ?1")
    void subCategoryPrice(String oldParentId, Integer price, Integer numberOfChildren);

    @Transactional
    @Modifying
    @Query("update ItemEntity i set i.numberOfChildren = i.numberOfChildren + ?2 where i.id = ?1")
    void addChildren(String parentId, Integer i);

    @Transactional
    @Modifying
    @Query("update ItemEntity i set i.numberOfChildren = i.numberOfChildren - ?2 where i.id = ?1")
    void subChildren(String parentId, Integer i);

    @Transactional
    @Modifying
    @Query("update ItemEntity i set i.date = ?2 where i.id = ?1")
    void updateDate(String parentId, ZonedDateTime updateDate);

    @Transactional
    @Query("select i from ItemEntity i where i.date >= ?1 and i.date <= ?2")
    List<ItemEntity> getSales(ZonedDateTime after, ZonedDateTime before);
}

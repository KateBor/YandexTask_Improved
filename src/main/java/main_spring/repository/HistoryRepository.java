package main_spring.repository;

import main_spring.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {

    @Transactional
    @Query("select h from HistoryEntity h where h.itemId = ?1 and h.date >= ?2 and h.date <= ?3")
    List<HistoryEntity> getStatistic(String id, ZonedDateTime zonedDateTimeFrom, ZonedDateTime zonedDateTimeTo);

    @Transactional
    @Modifying
    @Query("delete from HistoryEntity h where h.itemId = ?1")
    void deleteItemsById(String id);
}

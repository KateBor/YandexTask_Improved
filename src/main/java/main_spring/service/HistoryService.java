package main_spring.service;

import main_spring.entity.HistoryEntity;

import java.time.ZonedDateTime;
import java.util.List;

public interface HistoryService {

    List<HistoryEntity> getStatistic(String id, ZonedDateTime zonedDateTimeFrom, ZonedDateTime zonedDateTimeTo);
}

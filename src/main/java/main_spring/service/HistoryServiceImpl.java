package main_spring.service;

import lombok.RequiredArgsConstructor;
import main_spring.entity.HistoryEntity;
import main_spring.repository.HistoryRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    @Override
    public List<HistoryEntity> getStatistic(String id, ZonedDateTime zonedDateTimeFrom, ZonedDateTime zonedDateTimeTo) {
        return historyRepository.getStatistic(id, zonedDateTimeFrom, zonedDateTimeTo);
    }
}

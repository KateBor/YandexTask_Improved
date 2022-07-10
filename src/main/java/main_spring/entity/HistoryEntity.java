package main_spring.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Table(name = "history")
@NoArgsConstructor
public class HistoryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "item_type", nullable = false)
    private String type;

    @Column(name = "update_date", nullable = false)
    private ZonedDateTime date;

    public String getIsoDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String isoDate = date.format(dtf);
        isoDate = isoDate.replaceAll("\\+\\d{4}$", "Z");
        return isoDate;
    }

    @Column(name = "price")
    private Integer price;

    public HistoryEntity(ItemEntity item) {
        itemId = item.getId();
        type = item.getType().toString();
        name = item.getName();
        date = item.getDate();
        if (item.getPrice() == 0) {
            price = null;
        } else {
            price = item.getPrice();
        }
        if (item.getParentId() != null) {
            parentId = item.getParentId().getId();
        } else {
            parentId = null;
        }
    }
}

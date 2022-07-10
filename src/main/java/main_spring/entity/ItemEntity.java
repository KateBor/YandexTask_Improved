package main_spring.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import main_spring.web.request.ShopUnitType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static main_spring.web.request.ShopUnitType.OFFER;

@Entity
@Getter
@Table(name = "item")
@NoArgsConstructor
public class ItemEntity implements Serializable {

    @JsonProperty("type")
    @Column(name = "type", nullable = false)
    private ShopUnitType type;

    @JsonProperty("name")
    @Column(name = "name", nullable = false)
    private String name;

    @Id
    @JsonProperty("id")
    @Column(name = "id", nullable = false)
    private String id;

    @JsonIgnore
    @Column(name = "number_of_children", nullable = false)
    private Integer numberOfChildren;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="parent_id", referencedColumnName="id")
    @JsonManagedReference
    private ItemEntity parentId;

    @JsonProperty("parentId")
    public String getParentIdForJson() {
        if (parentId != null) {
            return parentId.id;
        }
        return null;
    }

    @JsonIgnore
    @Column(name = "price", nullable = false)
    private Integer price;

    @JsonProperty("price")
    public Integer getPriceForJson() {
        if (price == 0) {
            return null;
        }
        return price;
    }

    @JsonIgnore
    @Column(name = "update_date", nullable = false)
    private ZonedDateTime date;

    @JsonProperty("date")
    public String getIsoDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String isoDate = date.format(dtf);
        isoDate = isoDate.replaceAll("\\+\\d{4}$", "Z");
        return isoDate;
    }

    @JsonIgnore
    @JsonBackReference
    @OneToMany(mappedBy = "parentId", cascade = CascadeType.REMOVE)
    private List<ItemEntity> children = new ArrayList<>();

    @JsonProperty("children")
    public List<ItemEntity> getChildrenForJson() {
        if (children.isEmpty() && Objects.equals(type, OFFER)) {
            return null;
        }
        return children;
    }

    public ItemEntity(ShopUnitType type, String id, String name, Integer price, ItemEntity parentId, ZonedDateTime updateDate) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.price = price;
        this.parentId = parentId;
        this.date = updateDate;
        numberOfChildren = 0;
        children = null;
    }

    @Override
    public String toString() {
        return "{" +
                "type: " + type +
                ", name: " + name +
                ", id: " + id +
                ", parentId: " + getParentIdForJson() +
                ", price: " + getPriceForJson() +
                ", date: " + getIsoDate() +
                ", children: " + getChildrenForJson() +
                '}';
    }
}

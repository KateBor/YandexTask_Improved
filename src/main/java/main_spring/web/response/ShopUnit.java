package main_spring.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import main_spring.entity.ItemEntity;

import java.io.Serializable;
import java.util.List;


@Builder
public class ShopUnit implements Serializable {

    @JsonProperty("type")
    private String type;

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String id;

    @JsonProperty("parentId")
    private String parentId;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("date")
    private String date;

    @JsonProperty("children")
    private List<ItemEntity> children;

}

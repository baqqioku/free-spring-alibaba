package com.freedom.consumer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {
    private Integer id;
    private String name;
}

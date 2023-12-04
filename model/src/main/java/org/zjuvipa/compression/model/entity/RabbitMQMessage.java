package org.zjuvipa.compression.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RabbitMQMessage {
    private String client;
    private String message;
}

package com.github.bkwak.organizer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

@JsonDeserialize(using = OrderDeserializer.class)
public class Order {
    private String orderId;
    private BigDecimal orderValue;
    private LocalTime completeBy;
    private Picker picker;
    private LocalTime pickingTime;

    public Order() {
    }

    public Order(String orderId, BigDecimal orderValue, LocalTime completeBy, Picker picker, LocalTime pickingTime) {
        this.orderId = orderId;
        this.orderValue = orderValue;
        this.completeBy = completeBy;
        this.picker = picker;
        this.pickingTime = pickingTime;
    }

    // getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(BigDecimal orderValue) {
        this.orderValue = orderValue;
    }

    public LocalTime getCompleteBy() {
        return completeBy;
    }

    public void setCompleteBy(LocalTime completeBy) {
        this.completeBy = completeBy;
    }

    public Picker getPicker() {
        return picker;
    }

    public void setPicker(Picker picker) {
        this.picker = picker;
    }

    public LocalTime getPickingTime() {
        return pickingTime;
    }

    public void setPickingTime(LocalTime pickingTime) {
        this.pickingTime = pickingTime;
    }


}

class OrderDeserializer extends JsonDeserializer<Order> {


    @Override
    public Order deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        try {
            p.nextValue(); // skip orderId field name
            String orderId = p.getText(); // read orderId value as String
            System.out.println("orderId: " + orderId);
            p.nextValue(); // skip orderValue field name
            BigDecimal orderValue = new BigDecimal(p.getValueAsString()); // read orderValue value
            System.out.println("orderValue: " + orderValue);
            p.nextValue(); // skip pickingTime field name
            String pickingTimeString = p.getText(); // read pickingTime value as string
            Duration pickingDuration = Duration.parse(pickingTimeString);
            LocalTime pickingTime = LocalTime.MIN.plus(pickingDuration);
            System.out.println("pickingTime: " + pickingTime);
            p.nextValue(); // skip completeBy field name
            LocalTime completeBy = LocalTime.parse(p.getValueAsString()); // read completeBy value
            System.out.println("completeBy: " + completeBy);
            p.nextValue(); // skip picker field name
            Picker picker = new Picker();

            return new Order(orderId, orderValue, completeBy, picker, pickingTime);
        } catch (Exception e) {
            throw new IOException("Error deserializing Order at " + p.getCurrentLocation() + ": " + e.getMessage(), e);
        }
    }
}

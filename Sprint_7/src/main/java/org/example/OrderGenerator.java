package org.example;

import java.util.List;

public class OrderGenerator {
    public static Order getOrder(List<String> color) {
        final String firstName = "Екатерина";
        final String lastName = "Пушкина";
        final String address = "Дмитровское шоссе";
        final String metroStation = "Селигерская";
        final String phone = "+79996542343";
        final String rentTime = "2";
        final String deliveryDate = "2023-07-10";
        final String comment = "Оставить у подъезда";

        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }
}
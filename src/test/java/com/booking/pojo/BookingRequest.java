package com.booking.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private Integer roomid;
    private String firstname;
    private String lastname;
    private Boolean depositpaid;
    private BookingDates bookingdates;
    private String email;
    private String phone;
}

package com.dbproject.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchByCityDto {

    private String arriveCity;

    private LocalDate departDate;

    private LocalDate arriveDate;

    public SearchByCityDto() {
    }

    public SearchByCityDto(String arriveCity) {
        this.arriveCity = arriveCity;
    }

    public SearchByCityDto(String arriveCity, LocalDate departDate, LocalDate arriveDate) {
        this.arriveCity = arriveCity;
        this.departDate = departDate;
        this.arriveDate = arriveDate;
    }

    @Override
    public String toString() {
        return "SearchByCityDto{" +
                "arriveCity='" + arriveCity + '\'' +
                ", departDate=" + departDate +
                ", arriveDate=" + arriveDate +
                '}';
    }
}

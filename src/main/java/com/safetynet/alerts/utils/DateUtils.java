package com.safetynet.alerts.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

public class DateUtils {

    /**
     * return now() in LocalDate Format using ZoneId system
     * @return now in LocalDate Format
     */
    public LocalDate getNowLocalDate()
    {
        return LocalDate.now(ZoneId.systemDefault());
    }

    /**
     * Calcul age based on a birthdate LocalDate
     * @param birthDate birthDate
     * @return calculated age
     */
    public int getAge(LocalDate birthDate)
    {
        return Period.between(birthDate,this.getNowLocalDate()).getYears();
    }
}

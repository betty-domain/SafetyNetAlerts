package com.safetynet.alerts;

import com.safetynet.alerts.utils.DateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DateUtilsTests {

    private static DateUtils dateUtils;

    private static LocalDate nowLocalDateMock;

    @BeforeAll
    private static void setUpAllTest()
    {

    }

    @BeforeEach
    private void  setUpEachTest() {
        dateUtils = spy(new DateUtils());

        nowLocalDateMock = LocalDate.of(2015,10,5);
        when(dateUtils.getNowLocalDate()).thenReturn(nowLocalDateMock);
    }

    @Test
    public void getAgeTestDateBirthWithNullValues()
    {
        assertThat(dateUtils.getAge(null)).isEqualTo(Integer.MIN_VALUE);
    }


    @Test
    public void getAgeTestDateBirthInFutur()
    {
        LocalDate datebirth = nowLocalDateMock.plusYears(1);
        assertThat(dateUtils.getAge(datebirth)).isLessThan(0);
    }

    @Test
    public void getAgeTestLessThanOneMonth()
    {
        LocalDate datebirth = nowLocalDateMock.minusDays(1);
        assertThat(dateUtils.getAge(datebirth)).isEqualTo(0);
    }

    @Test
    public void getAgeTestLessThanOneYear()
    {
        LocalDate datebirth = nowLocalDateMock.minusMonths(3);
        assertThat(dateUtils.getAge(datebirth)).isEqualTo(0);
    }

    @Test
    public void getAgeTestEqualsOneYear()
    {
        LocalDate datebirth = nowLocalDateMock.minusYears(1);
        assertThat(dateUtils.getAge(datebirth)).isEqualTo(1);
    }

    @Test
    public void getAgeTestBetweenOneAndTwoYear()
    {
        LocalDate datebirth = nowLocalDateMock.minusYears(1).minusMonths(3);
        assertThat(dateUtils.getAge(datebirth)).isEqualTo(1);
    }

    @Test
    public void getAgeTestMoreThanOneYear()
    {
        LocalDate datebirth = nowLocalDateMock.minusYears(15).minusMonths(3);
        assertThat(dateUtils.getAge(datebirth)).isEqualTo(15);
    }

}

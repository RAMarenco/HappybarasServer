package org.happybaras.server.utils;

import org.happybaras.server.domain.entities.Entry;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EntriesFilters {

    public Map<Integer, Integer> mapEntriesByYear (List<Entry> entries) {
        Map<Integer, Integer> map = new HashMap<>();

        map.put(2020, getSumOfYear(entries, 2020));
        map.put(2021, getSumOfYear(entries, 2021));
        map.put(2022, getSumOfYear(entries, 2022));
        map.put(2023, getSumOfYear(entries, 2023));
        map.put(2024, getSumOfYear(entries, 2024));

        return map;
    }

    public Map<String, Integer> mapEntriesByWeek (List<Entry> entries) {
        Map<String, Integer> map = new HashMap<>();

        map.put(DayOfWeek.MONDAY.name(), getSumOfDay(entries, DayOfWeek.MONDAY));
        map.put(DayOfWeek.TUESDAY.name(), getSumOfDay(entries, DayOfWeek.TUESDAY));
        map.put(DayOfWeek.WEDNESDAY.name(), getSumOfDay(entries, DayOfWeek.WEDNESDAY));
        map.put(DayOfWeek.THURSDAY.name(), getSumOfDay(entries, DayOfWeek.THURSDAY));
        map.put(DayOfWeek.FRIDAY.name(), getSumOfDay(entries, DayOfWeek.FRIDAY));
        map.put(DayOfWeek.SATURDAY.name(), getSumOfDay(entries, DayOfWeek.SATURDAY));
        map.put(DayOfWeek.SUNDAY.name(), getSumOfDay(entries, DayOfWeek.SUNDAY));

        return map;
    }
    public Map<String, Integer> mapEntriesByMonths (List<Entry> entries) {
        Map<String, Integer> map = new HashMap<>();

        map.put(Month.JANUARY.name(), getSumOfMonth(entries, Month.JANUARY));
        map.put(Month.FEBRUARY.name(), getSumOfMonth(entries, Month.FEBRUARY));
        map.put(Month.MARCH.name(), getSumOfMonth(entries, Month.MARCH));
        map.put(Month.APRIL.name(), getSumOfMonth(entries, Month.APRIL));
        map.put(Month.MAY.name(), getSumOfMonth(entries, Month.MAY));
        map.put(Month.JUNE.name(), getSumOfMonth(entries, Month.JUNE));
        map.put(Month.JULY.name(), getSumOfMonth(entries, Month.JULY));
        map.put(Month.AUGUST.name(), getSumOfMonth(entries, Month.AUGUST));
        map.put(Month.SEPTEMBER.name(), getSumOfMonth(entries, Month.SEPTEMBER));
        map.put(Month.OCTOBER.name(), getSumOfMonth(entries, Month.OCTOBER));
        map.put(Month.NOVEMBER.name(), getSumOfMonth(entries, Month.NOVEMBER));
        map.put(Month.DECEMBER.name(), getSumOfMonth(entries, Month.DECEMBER));

        return map;
    }

    private Integer getSumOfYear (List<Entry> entries, Integer year) {
        return entries
                .stream()
                .filter(e -> e.getTimestamp().getYear() == year)
                .toList()
                .size();
    }

    private Integer getSumOfDay (List<Entry> entries, DayOfWeek day) {
        return entries
                .stream()
                .filter(e -> e.getTimestamp().getDayOfWeek().equals(day))
                .toList()
                .size();
    }

    private Integer getSumOfMonth (List<Entry> entries, Month month) {
        return entries
                .stream()
                .filter(e -> e.getTimestamp().getMonth().equals(month))
                .toList()
                .size();
    }
}

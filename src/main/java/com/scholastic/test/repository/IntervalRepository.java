package com.scholastic.test.repository;

import com.scholastic.test.domain.Interval;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository to store the created Intervals.
 * Uses a simple internal map, does not access any db.
 *
 * @author cingham
 */
@Service
public class IntervalRepository {

    private final Map<String, List<Interval>> intervalMap = new HashMap<>();

    public void create(String documentId, Interval interval) {
        if (!intervalMap.containsKey(documentId)) {
            List<Interval> intervalList = new ArrayList<>();
            intervalMap.put(documentId, intervalList);
        }

        List<Interval> intervalList = intervalMap.get(documentId);
        intervalList.add(interval);
    }

    public List<Interval> getAll(String documentId) {
        return intervalMap.get(documentId);
    }

    public void delete(String documentId, Interval interval) {
        List<Interval> intervalList = intervalMap.get(documentId);
        if (intervalList != null) {
            intervalList.remove(interval);
        }
    }
}

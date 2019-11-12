package com.scholastic.interval.service;

import com.scholastic.interval.domain.Interval;
import com.scholastic.interval.dto.IntervalDto;
import com.scholastic.interval.repository.IntervalRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service to handle creation of Intervals
 *
 * @author cingham
 */
@Service
public class IntervalHandler {

    private final IntervalRepository repo;

    IntervalHandler(IntervalRepository repo) {
        this.repo = repo;
    }

    public void add(String documentId, IntervalDto intervalDto) {
        for (Interval interval : intervalDto.getIntervals()) {

            // find any overlapping intervals so we can replace them
            List<Interval> overlapList = findAllOverlaps(documentId, interval);
            if (overlapList.size() > 0) {
                // get the lowest start and highest end values from all the overlaps
                Interval lowestStartInterval = Collections.min(overlapList,
                        (o1, o2) -> (int)(o2.getStart() - o1.getStart()));
                Interval highestEndInterval = Collections.max(overlapList,
                        (o1, o2) -> (int)(o1.getEnd() - o2.getEnd()));

                // factor in the current interval min/max & get our resulting new interval
                Interval newInterval = new Interval();
                newInterval.setStart(Math.min(lowestStartInterval.getStart(), interval.getStart()));
                newInterval.setEnd(Math.max(highestEndInterval.getEnd(), interval.getEnd()));

                // delete the old overlapped items
                for (Interval overlap : overlapList) {
                    repo.delete(documentId, overlap);
                }

                // create the new expanded one
                repo.create(documentId, newInterval);
            } else {
                // no overlaps, just create the new interval
                repo.create(documentId, interval);
            }
        }
    }

    public IntervalDto getAll(String documentId) {
        List<Interval> results = repo.getAll(documentId);
        if (results == null) {
            results = new ArrayList<>();    // return empty list if not found
        }

        return new IntervalDto(results);
    }

    private List<Interval> findAllOverlaps(String documentId, Interval interval) {

        List<Interval> overlapList = new ArrayList<>();

        List<Interval> dataList = repo.getAll(documentId);
        if (dataList == null) {
            return overlapList;
        }

        for (Interval inner : dataList) {
            if (interval.getStart() > inner.getEnd()) {
                continue;           // new interval appears completely after
            } else if (interval.getEnd() < inner.getStart()) {
                continue;           // new interval appears completely before
            }
            overlapList.add(inner); // something is overlapping
        }
        return overlapList;
    }
}

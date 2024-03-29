package com.scholastic.interval.dto;

import com.scholastic.interval.domain.Interval;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 *  Object to represent Interval values (as JSON) sent to/from the server
 *
 * @author cingham
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IntervalDto {
    List<Interval> intervals;
}

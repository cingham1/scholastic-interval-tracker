package com.scholastic.test.domain;

import lombok.*;

/**
 *  Entity object to represent an Interval
 *
 * @author cingham
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Interval {
    long start;
    long end;
}

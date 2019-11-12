package com.scholastic.test.web;

import com.scholastic.test.dto.HelloDto;
import com.scholastic.test.dto.IntervalDto;
import com.scholastic.test.dto.ServerResponseDto;
import com.scholastic.test.service.IntervalHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST contoller to put / get the intervals defined for a given document
 *
 * @author cingham
 */
@RestController
public class IntervalController {

    private final IntervalHandler intervalHandler;

    public IntervalController(IntervalHandler intervalHandler) {
        this.intervalHandler = intervalHandler;
    }

    @GetMapping(value = "/hello", produces = "application/json")
    public HelloDto hello() throws Exception {
        return new HelloDto();
    }

    @PostMapping(value = "/document/{documentId}/interval", consumes = "application/json",
            produces = "application/json")
    public ServerResponseDto postInterval(@PathVariable String documentId,
                                            @RequestBody IntervalDto intervalDto) throws Exception {
        intervalHandler.add(documentId, intervalDto);
        return new ServerResponseDto(HttpStatus.OK.value(), "Success");
    }

    @GetMapping(value = "/document/{documentId}/interval", produces = "application/json")
    public IntervalDto getIntervals(@PathVariable String documentId) throws Exception {
        IntervalDto intervalDto = intervalHandler.getAll(documentId);
        return intervalDto;
    }

}

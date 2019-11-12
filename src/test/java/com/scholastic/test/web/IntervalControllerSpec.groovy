package com.scholastic.test.web

import com.scholastic.test.domain.Interval
import com.scholastic.test.dto.IntervalDto
import com.scholastic.test.dto.ServerResponseDto
import com.scholastic.test.service.IntervalHandler
import spock.lang.Specification
import spock.lang.Subject

class IntervalControllerSpec extends Specification {

	@Subject
	IntervalController controller
	IntervalHandler handler
	String documentId
	List<Interval> intervalList
	IntervalDto nonEmptyDto
	IntervalDto emptyDto

	def "setup"() {
		documentId = "id-500"
		intervalList = Arrays.asList(new Interval(10, 20), new Interval(30, 40))
		nonEmptyDto = new IntervalDto(intervalList)
		emptyDto = new IntervalDto(new ArrayList<Interval>())
		handler = Mock()
		controller = new IntervalController(handler)
	}

	def "test post - no exception returns success"() {
 		when:
			ServerResponseDto dto = controller.postInterval(documentId, nonEmptyDto)
		then:
			dto.getStatus() == 200
			dto.getMessage().contains("Success")
	}

	def "test post - thrown exception returns exception (later handled by Spring)"() {
		given:
			handler.add(_,_) >> { throw new Exception("error"); }
		when:
			ServerResponseDto dto = controller.postInterval(documentId, intervalDto)
		then:
			thrown Exception
	}

	def "test get - empty list"() {
		given:
			handler.getAll(_) >> emptyDto
		when:
			IntervalDto dto = controller.getIntervals(documentId)
		then:
			dto.getIntervals().size() == 0
	}

	def "test get - items in list"() {
		given:
			handler.getAll(_) >> nonEmptyDto
		when:
			IntervalDto dto = controller.getIntervals(documentId)
		then:
			dto.getIntervals().size() == 2
	}

}

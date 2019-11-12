package com.scholastic.interval.service

import com.scholastic.interval.domain.Interval
import com.scholastic.interval.dto.IntervalDto
import com.scholastic.interval.repository.IntervalRepository
import spock.lang.Specification
import spock.lang.Subject

class IntervalHandlerSpec extends Specification {

	@Subject
	IntervalHandler handler
	IntervalRepository repo
	String documentId = "id-500"

	def "setup"() {
		repo = Mock()
		handler = new IntervalHandler(repo)
	}

	def "test add - no list exists yet"() {
		given:
			List<Interval> startRepoList = null
			List<Interval> inputList = Arrays.asList(new Interval(30, 40), new Interval(70, 80))
			repo.getAll(_) >> startRepoList
		when:
			IntervalDto dto = handler.add(documentId, new IntervalDto(inputList))
		then:
			2 * repo.create(_,_)
			0 * repo.delete(_,_)
	}

	def "test add - existing items do not overlap"() {
		given:
			List<Interval> startRepoList = Arrays.asList(new Interval(10, 20), new Interval(50, 60))
			List<Interval> inputList = Arrays.asList(new Interval(30, 40), new Interval(70, 80))
			repo.getAll(_) >> startRepoList
		when:
			IntervalDto dto = handler.add(documentId, new IntervalDto(inputList))
		then:
			2 * repo.create(_,_)
			0 * repo.delete(_,_)
	}

	def "test add - existing items do overlap"() {
		given:
			List<Interval> startRepoList = Arrays.asList(new Interval(10, 20), new Interval(50, 60))
			List<Interval> inputList = Arrays.asList(new Interval(15, 25), new Interval(70, 80))
			repo.getAll(_) >> startRepoList
		when:
			IntervalDto dto = handler.add(documentId, new IntervalDto(inputList))
		then:
			2 * repo.create(_,_)
			1 * repo.delete(_,_)
	}


	// Due to the nature of the storage scheme for this exercise there is some coupling
	// between the IntervalHandler and the IntervalRepository
	// (the service loads the existing list all at once, but saves/deletes them one at a time).
	// So we'll include some integration tests (not mocked) between the IntervalHandler and the
	// IntervalRepository to properly test the state of the underlying repo in between inserts/deletes
	def "integration test - partial overlap of one"() {
		given:
			handler = new IntervalHandler(new IntervalRepository())

			List<Interval> startingStateList = Arrays.asList(
					new Interval(10, 20),
					new Interval(50, 60))

			List<Interval> newInputList = Arrays.asList(
					new Interval(15, 25),	// overlaps on one end
					new Interval(30, 40))	// no overlap

			List<Interval> expectedResultsList = Arrays.asList(
					new Interval(10, 25),
					new Interval(30, 40),
					new Interval(50, 60))
		when:
			handler.add(documentId, new IntervalDto(startingStateList))
			handler.add(documentId, new IntervalDto(newInputList))
			IntervalDto dto = handler.getAll(documentId)
			List<Interval> list = dto.getIntervals()
		then:
			list.size() == expectedResultsList.size()
			for (Interval interval : expectedResultsList) {
				list.contains(interval)
			}
	}

	def "integration test - partial overlap of two"() {
		given:
			handler = new IntervalHandler(new IntervalRepository())

			List<Interval> startingStateList = Arrays.asList(
					new Interval(10, 20),
					new Interval(30, 40))

			List<Interval> newInputList = Arrays.asList(
					new Interval(15, 35), 	// overlaps two Intervals, one at each end
					new Interval(50, 60))	// no overlap

			List<Interval> expectedResultsList = Arrays.asList(
					new Interval(10, 40),
					new Interval(50, 60))
		when:
			handler.add(documentId, new IntervalDto(startingStateList))
			handler.add(documentId, new IntervalDto(newInputList))
			IntervalDto dto = handler.getAll(documentId)
			List<Interval> list = dto.getIntervals()
		then:
			list.size() == expectedResultsList.size()
			for (Interval interval : expectedResultsList) {
				list.contains(interval)
			}
	}

	def "integration test - inner/outer overlap"() {
		given:
			handler = new IntervalHandler(new IntervalRepository())

			List<Interval> startingStateList = Arrays.asList(
					new Interval(10, 20),
					new Interval(40, 50))

			List<Interval> newInputList = Arrays.asList(
					new Interval(12, 15), 	// inner overlap
					new Interval(35, 55))	// outer overlap

			List<Interval> expectedResultsList = Arrays.asList(
					new Interval(10, 20),
					new Interval(35, 55))
		when:
			handler.add(documentId, new IntervalDto(startingStateList))
			handler.add(documentId, new IntervalDto(newInputList))
			IntervalDto dto = handler.getAll(documentId)
			List<Interval> list = dto.getIntervals()
		then:
			list.size() == expectedResultsList.size()
			for (Interval interval : expectedResultsList) {
				list.contains(interval)
			}
	}

	def "integration test - outer overlap of multiple"() {
		given:
			handler = new IntervalHandler(new IntervalRepository())

			List<Interval> startingStateList = Arrays.asList(
					new Interval(10, 20),
					new Interval(30, 40),
					new Interval(50, 60))

			List<Interval> newInputList = Arrays.asList(
					new Interval(10, 55), 	// overlaps 3 items
					new Interval(80, 90))	// no overlap

			List<Interval> expectedResultsList = Arrays.asList(
					new Interval(10, 55),
					new Interval(80, 90))
		when:
			handler.add(documentId, new IntervalDto(startingStateList))
			handler.add(documentId, new IntervalDto(newInputList))
			IntervalDto dto = handler.getAll(documentId)
			List<Interval> list = dto.getIntervals()
		then:
			list.size() == expectedResultsList.size()
			for (Interval interval : expectedResultsList) {
				list.contains(interval)
			}
	}

	def "integration test - touching overlap"() {
		given:
			handler = new IntervalHandler(new IntervalRepository())

			List<Interval> startingStateList = Arrays.asList(
					new Interval(10, 20),
					new Interval(40, 50))

			List<Interval> newInputList = Arrays.asList(
					new Interval(20, 25), 	// left side touching
					new Interval(35, 40))	// right side touching

			List<Interval> expectedResultsList = Arrays.asList(
					new Interval(10, 25),
					new Interval(35, 50));
		when:
			handler.add(documentId, new IntervalDto(startingStateList))
			handler.add(documentId, new IntervalDto(newInputList))
			IntervalDto dto = handler.getAll(documentId)
			List<Interval> list = dto.getIntervals()
		then:
			list.size() == expectedResultsList.size()
			for (Interval interval : expectedResultsList) {
				list.contains(interval)
			}
	}

}

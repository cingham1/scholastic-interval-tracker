package com.scholastic.test.repository

import com.scholastic.test.domain.Interval
import spock.lang.Specification
import spock.lang.Subject

class IntervalRepositorySpec extends Specification {

	@Subject
	IntervalRepository repo
	String documentId1 = "id-501"
	String documentId2 = "id-502"

	def "setup"() {
		repo = new IntervalRepository()
	}

	def "test create - add one"() {
		given:
			Interval interval = new Interval(30, 40)
		when:
			repo.create(documentId1, interval)
		then:
			repo.intervalMap.get(documentId1) != null
			repo.intervalMap.get(documentId1).size() == 1
			repo.intervalMap.get(documentId2) == null
	}

	def "test create - add multiple"() {
		given:
			Interval interval1 = new Interval(30, 40)
			Interval interval2 = new Interval(50, 60)
		when:
			repo.create(documentId1, interval1)
			repo.create(documentId1, interval2)
		then:
			repo.intervalMap.get(documentId1) != null
			repo.intervalMap.get(documentId1).size() == 2
			repo.intervalMap.get(documentId2) == null
	}

	def "test getAll - empty list returns null"() {
		when:
			List<Interval> list = repo.getAll(documentId1)
		then:
			list == null
	}

	def "test getAll - multiple values"() {
		given:
			Interval interval1 = new Interval(30, 40)
			Interval interval2 = new Interval(50, 60)
		when:
			repo.create(documentId1, interval1)
			repo.create(documentId1, interval2)
			List<Interval> list = repo.getAll(documentId1)
		then:
			list.size() == 2
			list.contains(interval1)
			list.contains(interval2)
	}

	def "test delete"() {
		given:
			Interval interval1 = new Interval(30, 40)
			Interval interval2 = new Interval(50, 60)
			Interval interval3 = new Interval(70, 80)
		when:
			repo.create(documentId1, interval1)
			repo.create(documentId1, interval2)
			repo.create(documentId1, interval3)
			repo.delete(documentId1, interval2)
			List<Interval> list = repo.getAll(documentId1)
		then:
			list.contains(interval1)
			!list.contains(interval2)
			list.contains(interval3)
	}

	def "test delete item that does not exist does not throw exception"() {
		given:
			Interval interval1 = new Interval(30, 40)
			Interval interval2 = new Interval(50, 60)
			Interval interval3 = new Interval(70, 80)
		when:
			repo.create(documentId1, interval1)
			repo.create(documentId1, interval2)
			repo.delete(documentId1, interval3)
			List<Interval> list = repo.getAll(documentId1)
		then:
			list.contains(interval1)
			list.contains(interval2)
			!list.contains(interval3)
	}

	def "test delete from one document does not affect another document"() {
		given:
			Interval interval1 = new Interval(30, 40)
			Interval interval2 = new Interval(50, 60)
			Interval interval3 = new Interval(70, 80)
		when:
			repo.create(documentId1, interval1)
			repo.create(documentId1, interval2)
			repo.create(documentId1, interval3)
			repo.delete(documentId1, interval1)
			repo.create(documentId2, interval1)
			repo.create(documentId2, interval2)
			repo.create(documentId2, interval3)
			repo.delete(documentId2, interval2)
			repo.delete(documentId2, interval3)
			List<Interval> list1 = repo.getAll(documentId1)
			List<Interval> list2 = repo.getAll(documentId2)
		then:
			list1.size() == 2
			!list1.contains(interval1)
			list1.contains(interval2)
			list1.contains(interval3)
			list2.size() == 1
			list2.contains(interval1)
			!list2.contains(interval2)
			!list2.contains(interval3)
	}
}

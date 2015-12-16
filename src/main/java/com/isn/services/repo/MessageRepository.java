package com.isn.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.isn.services.po.Message;

//@RepositoryRestResource(collectionResourceRel = "message", path = "message")
public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {

	@Query("SELECT m from Message m, User u join m.receivers r WHERE r.mobile = u.mobile or r.email = u.mobile and u.id = ?1")
	List<Message> findReceivedMessages(@Param("userId") long userId);
}
package com.isn.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.isn.services.po.Message;

//@RepositoryRestResource(collectionResourceRel = "message", path = "message")
public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {

	@Query("SELECT m from Message m WHERE m.sender.id = ?1")
	List<Message> findSendMessages(@Param("userId") long userId);
	
	//TODO
	@Query("SELECT m from Message m join m.receivers r WHERE r.user.id = ?1")
	List<Message> findReceivedMessages(@Param("userId") long userId);
}
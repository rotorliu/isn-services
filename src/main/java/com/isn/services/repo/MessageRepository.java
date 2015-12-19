package com.isn.services.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.isn.services.po.Message;

//@RepositoryRestResource(collectionResourceRel = "message", path = "message")
public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {

	Page<Message> findByOrderBySendTimeDesc(Pageable pageRequest);
	Page<Message> findByOrderBySendTimeAsc(Pageable pageRequest);
	
	@Query("SELECT m FROM Message m, User u join m.receivers r WHERE u.id = ?1 AND r.mobile = u.mobile ORDER BY m.sendTime DESC")
	Page<Message> findMyReceivedMessages(@Param("userId") long userId, Pageable pageRequest);
	
	@Query("SELECT m FROM Message m WHERE m.sender.id = ?1 ORDER BY m.sendTime DESC")
	Page<Message> findMySendMessages(@Param("userId") long userId, Pageable pageRequest);
	
	@Query("SELECT m FROM Message m, User u join u.inBox.messages uim WHERE u.id = ?1 AND uim.id = m.id ORDER BY m.sendTime DESC")
	Page<Message> findMyInBoxMessages(@Param("userId") long userId, Pageable pageRequest);
	
	@Query("SELECT m FROM Message m, User u join u.outBox.messages uom WHERE u.id = ?1 AND uom.id = m.id ORDER BY m.sendTime DESC")
	Page<Message> findMyOutBoxMessages(@Param("userId") long userId, Pageable pageRequest);
}
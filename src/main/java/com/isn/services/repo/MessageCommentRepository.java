package com.isn.services.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.isn.services.po.MessageComment;

public interface MessageCommentRepository extends PagingAndSortingRepository<MessageComment, Long> {
	
	@Query("SELECT mc from MessageComment mc WHERE mc.owner.id = :messageId ORDER BY mc.time DESC")
	Page<MessageComment> findMessageCommentsOrderByTimeDesc(@Param("messageId") long messageId, Pageable pageRequest);
	
	@Query("SELECT mc from MessageComment mc WHERE mc.commenter.id = :userId ORDER BY mc.time DESC")
	Page<MessageComment> findUserCommentsOrderByTimeDesc(@Param("userId") long userId, Pageable pageRequest);
}

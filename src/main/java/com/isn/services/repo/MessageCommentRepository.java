package com.isn.services.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.isn.services.po.MessageComment;

public interface MessageCommentRepository extends PagingAndSortingRepository<MessageComment, Long> {

}

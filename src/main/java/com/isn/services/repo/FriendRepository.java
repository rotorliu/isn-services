package com.isn.services.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.isn.services.po.Friend;

//@RepositoryRestResource(collectionResourceRel = "friend", path = "friend")
public interface FriendRepository extends PagingAndSortingRepository<Friend, Long> {
	@Query("SELECT f FROM Friend f WHERE f.owner.id = :userId ORDER BY f.alias ASC")
	Page<Friend> findMyFriendsOrderByAliasAsc(@Param("userId") long userId, Pageable pageRequest);
}
package com.isn.services.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.isn.services.po.Friend;

//@RepositoryRestResource(collectionResourceRel = "friend", path = "friend")
public interface FriendRepository extends PagingAndSortingRepository<Friend, Long> {

}
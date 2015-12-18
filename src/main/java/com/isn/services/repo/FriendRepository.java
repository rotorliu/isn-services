package com.isn.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.isn.services.po.Friend;
import com.isn.services.po.User;

//@RepositoryRestResource(collectionResourceRel = "friend", path = "friend")
public interface FriendRepository extends PagingAndSortingRepository<Friend, Long> {
	@Query("SELECT u from User u WHERE u.mobile = ?1 or u.email = ?2")
	List<User> concretize(@Param("mobile") String mobile, @Param("email") String email);
}
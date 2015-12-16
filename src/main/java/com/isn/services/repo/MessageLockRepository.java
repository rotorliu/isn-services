package com.isn.services.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.isn.services.po.MessageLock;

//@RepositoryRestResource(collectionResourceRel = "messagelock", path = "messagelock")
public interface MessageLockRepository extends PagingAndSortingRepository<MessageLock, Long>{

}

package com.ronanski11.teezinator.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ronanski11.teezinator.model.ConsumedTea;

@Repository
public interface ConsumedTeaRepository extends PagingAndSortingRepository<ConsumedTea, String>{
	
	ConsumedTea save(ConsumedTea tea);

	List<ConsumedTea> findByUser(String user);
	
	@Query(value = "{}", sort = "{'time': -1}")
	List<ConsumedTea> findLatestTeas(PageRequest pageRequest);
	
	default List<ConsumedTea> findLatestTeasPaging(int pageNumber) {
		return findLatestTeas(PageRequest.of(pageNumber, 10));
	}

}

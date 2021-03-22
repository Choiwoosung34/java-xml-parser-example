package com.sample.xmlparser.jockeys;

import com.sample.xmlparser.domain.Jockey;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface JockeyRepository extends PagingAndSortingRepository<Jockey, Long> {

}

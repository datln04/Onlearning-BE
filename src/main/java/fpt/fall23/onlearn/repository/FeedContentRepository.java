package fpt.fall23.onlearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fpt.fall23.onlearn.entity.FeedContent;

public interface FeedContentRepository extends JpaRepository<FeedContent, Long>{
	
}

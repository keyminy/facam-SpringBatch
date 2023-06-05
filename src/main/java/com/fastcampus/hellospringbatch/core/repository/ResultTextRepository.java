package com.fastcampus.hellospringbatch.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcampus.hellospringbatch.core.domain.PlainText;
import com.fastcampus.hellospringbatch.core.domain.ResultText;

public interface ResultTextRepository extends JpaRepository<ResultText, Integer>{
	

}

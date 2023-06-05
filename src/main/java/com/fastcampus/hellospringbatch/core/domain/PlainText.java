package com.fastcampus.hellospringbatch.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@Entity
@Data
@DynamicUpdate // 일부 컬럼의 값이 변경 되었을 때, 그 컬럼에 대해서만 쿼리를 할 수 있게 해줌
@Table(name = "plain_text")
public class PlainText {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String text;
	
	
	
}

package com.ronanski11.teezinator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Image {

	public Image(String baseString) {
		this.baseString = baseString;
	}

	@Id
	private String id;
	
	private String baseString;
	
}

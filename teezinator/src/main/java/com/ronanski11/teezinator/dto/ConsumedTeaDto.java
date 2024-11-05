package com.ronanski11.teezinator.dto;

import java.time.LocalDateTime;

import com.ronanski11.teezinator.model.EntryType;

import lombok.Data;

@Data
public class ConsumedTeaDto {
	
	private String teaId;
	
	private String timeOfConsumption;
	
	private String image;
	
	private String imagePreview;
	
	private boolean sugar;
	
	private EntryType type;

	public boolean getSugar() {
		return this.sugar;
	}
}

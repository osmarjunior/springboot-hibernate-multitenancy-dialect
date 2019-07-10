package com.osmar.multitenacy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "PRODUCT")
public class Product {
	
	@Id
	@Column(name = "PRODUCT_ID")
	private Integer id;

	@Column(name = "DESCRIPTION")
	private String description;
	
}

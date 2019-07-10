package com.osmar.multitenacy.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.osmar.multitenacy.config.TenantContext;
import com.osmar.multitenacy.main.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	public void shouldFindAllMultiTenant() {
		TenantContext.setTenantId("tenant_1", "oracle");
		
		productRepository.findAll(PageRequest.of(0, 2));
		
		TenantContext.setTenantId("tenant_2", "mssql");
		
		productRepository.findAll(PageRequest.of(0, 2));
	}
}
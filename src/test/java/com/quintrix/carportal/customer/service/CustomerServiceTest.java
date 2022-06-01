package com.quintrix.carportal.customer.service;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.quintrix.carportal.customer.entity.ClientCustomer;
import com.quintrix.carportal.customer.entity.Customer;
import com.quintrix.carportal.customer.exception.CustomerNotFoundException;
import com.quintrix.carportal.customer.repository.CustomerRepository;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CustomerServiceTest {

	@Mock
	private CustomerRepository customerRepository;
	
	@InjectMocks
	private CustomerServiceImpl customerService;
	
	// JUnit test for CustomerNotFoundException method
	@DisplayName("JUnit test for CustomerNotFoundException method")
	@Test
	public void givenCustomer_whenGetCustomerByEmail_thenCustomerNotFoundException() {
		Customer customer = new Customer();
		customer.setEmail("steve@gmail.com");
		
		System.out.println(customerRepository);
		System.out.println(customerService);
		
		org.junit.jupiter.api.Assertions.assertThrows(CustomerNotFoundException.class, () -> {
			customerService.getCustomerByEmail(customer.getEmail());
		});
		verify(customerRepository, never()).save(any(Customer.class));
	}
	
	// JUnit test for getAllCustomers method
	@DisplayName("JUnit test for getAllCustomers method")
	@Test
	public void getAllCustomersThenReturnCustomerListTest() {
		Customer customer1 = new Customer();
		Customer customer2 = new Customer();
		List<Customer> customerList = new ArrayList<>();
		customer1.setId(1L);
		customer1.setName("Steve");
		customer1.setEmail("steve@gmail.com");
		customer1.setPhoneNumber("202-918-2132");
		customer1.setActive(true);
		customer1.setAddress("DC");
		customer1.setOwnedCars(null);
		customer2.setId(2L);
		customer2.setName("James");
		customer2.setEmail("james@gmail.com");
		customer2.setPhoneNumber("582-282-4873");
		customer2.setActive(false);
		customer2.setAddress("PA");
		customer2.setOwnedCars(null);
		customerList.add(customer1);
		customerList.add(customer2);
		
		given(customerRepository.findAll()).willReturn(customerList);
		
		List<ClientCustomer> clientCustomerList = customerService.getAllCustomers();
		
		assertThat(clientCustomerList).isNotNull();
		assertThat(clientCustomerList.size()).isEqualTo(2);
	}
}

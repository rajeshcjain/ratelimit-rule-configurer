package com.blue.optima.config.rest;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.blue.optima.config.model.ThrottlingConfiguration;
import com.blue.optima.rule.config.rest.RuleConfigurerController;
import com.blue.optima.rule.config.service.RuleConfigurerService;

public class RuleConfigurerTest {

	@Mock
	private final RuleConfigurerService ruleConfigurerService = null;

	@InjectMocks
	private final RuleConfigurerController ruleConfigurer = new RuleConfigurerController();

	private ThrottlingConfiguration throttlingConfg = null;
	private ThrottlingConfiguration throttlingConfgForBadReq = null;

	private ArrayList<ThrottlingConfiguration> listThrottlingConfiguration = null;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		throttlingConfg = new ThrottlingConfiguration("u1", "post", "/api/v1/test", "test_module", 10, 60, "updated_Date", "active");
		throttlingConfgForBadReq = new ThrottlingConfiguration("u1", "", "/api/v1/test", "test_module", 10, 60, "updated_Date", "active");
		listThrottlingConfiguration = new ArrayList<>();
		listThrottlingConfiguration.add(new ThrottlingConfiguration("u1", "post", "/api/v1/test", "test_module", 10, 60, "updated_Date", "active"));
		listThrottlingConfiguration.add(new ThrottlingConfiguration("u1", "post", "/api/v2/test", "test_module", 10, 60, "updated_Date", "active"));
		listThrottlingConfiguration.add(new ThrottlingConfiguration("u1", "post", "/api/v3/test", "test_module", 10, 60, "updated_Date", "active"));
	}

	@Test
	public void testCreateRateConfigRequest() {
		Mockito.when(ruleConfigurerService.saveThrottlingConfiguration(throttlingConfg)).thenReturn(new Integer(1));
		final ResponseEntity<String> status = ruleConfigurer.createRateConfigRequest(throttlingConfg);
		Assert.assertEquals(201, status.getStatusCodeValue());
	}

	@Test
	public void testCreateRateConfigRequestForBadRequest() {
		final ResponseEntity<String> status = ruleConfigurer.createRateConfigRequest(throttlingConfgForBadReq);
		Assert.assertEquals(400, status.getStatusCodeValue());
	}

	@Test
	public void testCreateRateConfigRequestRet200() {
		Mockito.when(ruleConfigurerService.saveThrottlingConfiguration(throttlingConfg)).thenReturn(new Integer(0));
		final ResponseEntity<String> status = ruleConfigurer.createRateConfigRequest(throttlingConfg);
		Assert.assertEquals(200, status.getStatusCodeValue());
	}

	@Test
	public void testUpdateRateConfigRequest() {
		Mockito.when(ruleConfigurerService.updateThrottlingConfiguration(throttlingConfg)).thenReturn(new Integer(1));
		final ResponseEntity<String> status = ruleConfigurer.updateRateConfigRequest(throttlingConfg);
		Assert.assertEquals(201, status.getStatusCodeValue());
	}

	@Test
	public void testUpdateRateConfigRequestBadRequest() {
		final ResponseEntity<String> status = ruleConfigurer.updateRateConfigRequest(throttlingConfgForBadReq);
		Assert.assertEquals(400, status.getStatusCodeValue());
	}

	@Test
	public void testUpdateRateConfigRequestRet200() {
		Mockito.when(ruleConfigurerService.updateThrottlingConfiguration(throttlingConfg)).thenReturn(new Integer(0));
		final ResponseEntity<String> status = ruleConfigurer.updateRateConfigRequest(throttlingConfg);
		Assert.assertEquals(200, status.getStatusCodeValue());
	}

	@Test
	public void testGetRateConfigRequest() {
		Mockito.when(ruleConfigurerService.getThrottlingConfigurationForUser("u1")).thenReturn(listThrottlingConfiguration);
		Assert.assertEquals(listThrottlingConfiguration, ruleConfigurer.getRateConfigRequest("u1"));
	}

}

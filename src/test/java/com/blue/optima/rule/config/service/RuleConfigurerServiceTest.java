package com.blue.optima.rule.config.service;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.blue.optima.config.model.ThrottlingConfiguration;
import com.blue.optima.rule.config.persistence.RuleConfigurerPersistenceService;

public class RuleConfigurerServiceTest {

	@Mock
	private RuleConfigurerPersistenceService ruleConfigurerPersistenceService;

	@InjectMocks
	private final RuleConfigurerService ruleConfigurerService = new RuleConfigurerService();

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
	public void testSaveThrottlingConfiguration() {
		Mockito.when(ruleConfigurerPersistenceService.create(throttlingConfg)).thenReturn(new Integer(1));
		final int status = ruleConfigurerService.saveThrottlingConfiguration(throttlingConfg);
		Assert.assertEquals(1, status);
	}

	@Test
	public void testUpdateThrottlingConfigurationn() {
		Mockito.when(ruleConfigurerPersistenceService.update(throttlingConfg)).thenReturn(new Integer(1));
		final int status = ruleConfigurerService.updateThrottlingConfiguration(throttlingConfg);
		Assert.assertEquals(1, status);
	}

	@Test
	public void testGetThrottlingConfigurationForUser() {
		Mockito.when(ruleConfigurerPersistenceService.getAllActiveCheckPointsOfUser("u1")).thenReturn(listThrottlingConfiguration);
		Assert.assertEquals(listThrottlingConfiguration, ruleConfigurerService.getThrottlingConfigurationForUser("u1"));
	}

	@Test
	public void testGetThrottlingConfigurationForUserInModule() {
		Mockito.when(ruleConfigurerPersistenceService.getAllActiveCheckPointsOfUserInModule("u1", "test-module")).thenReturn(listThrottlingConfiguration);
		Assert.assertEquals(listThrottlingConfiguration, ruleConfigurerService.getThrottlingConfigurationForUserInModule("u1", "test-module"));
	}

}

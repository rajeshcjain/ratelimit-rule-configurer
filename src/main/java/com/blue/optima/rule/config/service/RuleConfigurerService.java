package com.blue.optima.rule.config.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blue.optima.config.model.ThrottlingConfiguration;
import com.blue.optima.rule.config.persistence.RuleConfigurerPersistenceService;

@Service("ruleConfigurerService")
public class RuleConfigurerService {

	@Autowired
	private RuleConfigurerPersistenceService ruleConfigurerPersistenceService;

	public int saveThrottlingConfiguration(final ThrottlingConfiguration throttlingConfiguration) {
		return ruleConfigurerPersistenceService.create(throttlingConfiguration);
	}

	public int updateThrottlingConfiguration(final ThrottlingConfiguration throttlingConfiguration) {
		return ruleConfigurerPersistenceService.update(throttlingConfiguration);
	}

	public List<ThrottlingConfiguration> getThrottlingConfigurationForUser(final String userName) {
		final List<ThrottlingConfiguration> list = ruleConfigurerPersistenceService.getAllActiveCheckPointsOfUser(userName);
		if (list.size() == 0) {
			return Collections.emptyList();
		}
		return list;

	}

	public List<ThrottlingConfiguration> getThrottlingConfigurationForUserInModule(final String userName, final String module) {
		final List<ThrottlingConfiguration> list = ruleConfigurerPersistenceService.getAllActiveCheckPointsOfUserInModule(userName, module);
		if (list.size() == 0) {
			return Collections.emptyList();
		}
		return list;

	}

}

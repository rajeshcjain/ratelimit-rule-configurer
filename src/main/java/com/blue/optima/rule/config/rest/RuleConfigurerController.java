package com.blue.optima.rule.config.rest;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blue.optima.config.model.ThrottlingConfiguration;
import com.blue.optima.rule.config.service.RuleConfigurerService;

@RestController
@RequestMapping(value = "/api/v1", consumes = "application/json", produces = "application/json")
public class RuleConfigurerController {

	@Autowired
	RuleConfigurerService ruleConfigurerService;

	@Value("${rate.limiter.dafult.throttle.value}")
	private Integer defaultThrottleLimit;

	@Value("${rate.limiter.dafult.time.limit.value}")
	private Integer defaultTimeLimit;

	private boolean areParamsInvalid(final ThrottlingConfiguration throttlingConfg) {
		if (isBlank(throttlingConfg.getUsername()) || isBlank(throttlingConfg.getHttp_func()) || isBlank(throttlingConfg.getUri())) {
			return true;
		}
		return false;
	}

	private ThrottlingConfiguration updateMissingValuesWithDefaultValues(final ThrottlingConfiguration confg) {
		if (confg.getThrottle_limit() == null || confg.getTime_limit() == null) {
			return new ThrottlingConfiguration(confg.getUsername(), confg.getHttp_func(), confg.getUri(), confg.getModule(), defaultThrottleLimit, defaultTimeLimit, null, confg.getStatus());
		}
		return confg;
	}

	@PostMapping("/internal/rate/limiter/config")
	public ResponseEntity<String> createRateConfigRequest(@RequestBody final ThrottlingConfiguration throttlingConfg) {
		if (throttlingConfg == null || areParamsInvalid(throttlingConfg)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final int status = ruleConfigurerService.saveThrottlingConfiguration(updateMissingValuesWithDefaultValues(throttlingConfg));

		if (status == 1) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@PutMapping("/internal/rate/limiter/config")
	public ResponseEntity<String> updateRateConfigRequest(@RequestBody final ThrottlingConfiguration throttlingConfg) {
		if (throttlingConfg == null || areParamsInvalid(throttlingConfg)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final int status = ruleConfigurerService.updateThrottlingConfiguration(updateMissingValuesWithDefaultValues(throttlingConfg));

		if (status == 1) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/internal/rate/limiter/user/{user}")
	public List<ThrottlingConfiguration> getRateConfigRequest(@PathVariable("user") final String user) {
		return ruleConfigurerService.getThrottlingConfigurationForUser(user);
	}

}

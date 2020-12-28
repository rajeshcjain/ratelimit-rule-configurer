package com.blue.optima.rule.config.persistence;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.blue.optima.config.model.ThrottlingConfiguration;
import com.blue.optima.config.model.ThrottlingConfigurationOrmObj;

public class RuleConfigurerPersistenceServiceTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	private static String create = "insert into ratelimiter.configuration(username,http_func,uri,module,throttle_limit,time_limit,status) values(?,?,?,?,?,?,?)";
	private static String update = "update ratelimiter.configuration set username = ?,http_func = ?,uri = ?,module = ?,throttle_limit = ?,time_limit = ?,status = ? where id = ? ";

	private static String updateStatus = "update ratelimiter.configuration set status=? where id=?";
	private static String getEntry4Url = "select * from ratelimiter.configuration where uri = ? and http_func = ? and username = ? and module = ?";
	private static String updateThrollingLimit = "update ratelimiter.configuration set throttle_limit = ?,time_limit = ? where uri = ? and username = ? and module = ?";
	private static String getAllActiveCheckPoints = "select * from ratelimiter.configuration where status=? and username = ?";
	private static String getAllActiveCheckPoints4UserInModule = "select * from ratelimiter.configuration where status=? and username = ? and module = ?";

	private static String updateStatusOfUserForAllUrls = "update ratelimiter.configuration set status = ? where username = ?";

	private ThrottlingConfiguration throttlingConfg = null;
	private ThrottlingConfiguration throttlingConfgForBadReq = null;

	private ArrayList<ThrottlingConfiguration> listThrottlingConfiguration = null;
	private ArrayList<ThrottlingConfigurationOrmObj> listThrottlingConfigurationOrmObj = null;

	@InjectMocks
	private final RuleConfigurerPersistenceService ruleConfigurerPersistenceService = new RuleConfigurerPersistenceService();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		throttlingConfg = new ThrottlingConfiguration("u1", "post", "/api/v1/test", "test_module", 10, 60, "updated_Date", "active");
		throttlingConfgForBadReq = new ThrottlingConfiguration("u1", "", "/api/v1/test", "test_module", 10, 60, "updated_Date", "active");
		listThrottlingConfiguration = new ArrayList<>();
		listThrottlingConfiguration.add(new ThrottlingConfiguration("u1", "post", "/api/v1/test", "test_module", 10, 60, "updated_Date", "active"));
		listThrottlingConfiguration.add(new ThrottlingConfiguration("u1", "post", "/api/v2/test", "test_module", 10, 60, "updated_Date", "active"));
		listThrottlingConfiguration.add(new ThrottlingConfiguration("u1", "post", "/api/v3/test", "test_module", 10, 60, "updated_Date", "active"));
		listThrottlingConfigurationOrmObj = new ArrayList<>();
		listThrottlingConfigurationOrmObj.add(new ThrottlingConfigurationOrmObj(1, "u1", "post", "/api/v1/test", "test_module", 10, 60, "updated_Date", "active"));
		listThrottlingConfigurationOrmObj.add(new ThrottlingConfigurationOrmObj(2, "u1", "post", "/api/v2/test", "test_module", 10, 60, "updated_Date", "active"));
		listThrottlingConfigurationOrmObj.add(new ThrottlingConfigurationOrmObj(3, "u1", "post", "/api/v3/test", "test_module", 10, 60, "updated_Date", "active"));

	}

	@Test
	public void testUpdateStatusDataExistsInDB() {
		Mockito.when(jdbcTemplate.query(getEntry4Url, new ThrottlingConfigurationOrmObjRowMapper(), "/api/v1/test", "post", "u1", "test_module")).thenReturn(listThrottlingConfigurationOrmObj);
		Assert.assertEquals(0, ruleConfigurerPersistenceService.updateStatus("active", new ThrottlingConfiguration("u1", "post", "/api/v1/test", "test_module", 10, 60, "updated_Date", "active")));
	}

	@Test
	public void testUpdateStatusDataDosNotExistsInDb() {
		Mockito.when(jdbcTemplate.query(getEntry4Url, new ThrottlingConfigurationOrmObjRowMapper(), "/api/v1/test", "post", "u1", "test_module")).thenReturn(Collections.emptyList());
		Mockito.when(jdbcTemplate.update(create, "u1", "post", "/api/v1/test", 10, 60)).thenReturn(1);
		Assert.assertEquals(1, ruleConfigurerPersistenceService.updateStatus("active", new ThrottlingConfiguration("u1", "post", "/api/v1/test", "test_module", 10, 60, "updated_Date", "active")));
	}

	@Test
	public void testUpdateStatusDataExistsInDbWithDiffDataInDb() {
		Mockito.when(jdbcTemplate.query(getEntry4Url, new ThrottlingConfigurationOrmObjRowMapper(), "/api/v1/test", "post", "u1", "test_module")).thenReturn(listThrottlingConfigurationOrmObj);
		Mockito.when(jdbcTemplate.update(create, "u1", "post", "/api/v1/test", 10, 60)).thenReturn(1);
		Assert.assertEquals(1, ruleConfigurerPersistenceService.updateStatus("active", new ThrottlingConfiguration("u1", "post", "/api/v1/test", "test_module", 10, 60, "updated_Date", "active")));

	}
}

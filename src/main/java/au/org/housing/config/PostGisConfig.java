package au.org.housing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * PostGIS Connection Parameters and configuration
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */

@Component
@PropertySource("classpath:connections.properties")
public class PostGisConfig{
	
	@Value("${postgis_type}") 
	private String postgis_type;
	
	@Value("${postgis_host}") 
	private String postgis_host;
	
	@Value("${postgis_port}") 
	private Integer postgis_port;
	
	@Value("${postgis_schema}") 
	private String postgis_schema;
	
	@Value("${postgis_database}") 
	private String postgis_database;
	
	@Value("${postgis_user}") 
	private String postgis_user;
	
	@Value("${postgis_passwd}") 
	private String postgis_passwd;
	
	
	public String getPostgis_type() {
		return postgis_type;
	}
	public void setPostgis_type(String postgis_type) {
		this.postgis_type = postgis_type;
	}
	public String getPostgis_host() {
		return postgis_host;
	}
	public void setPostgis_host(String postgis_host) {
		this.postgis_host = postgis_host;
	}
	public Integer getPostgis_port() {
		return postgis_port;
	}
	public void setPostgis_port(Integer postgis_port) {
		this.postgis_port = postgis_port;
	}
	public String getPostgis_schema() {
		return postgis_schema;
	}
	public void setPostgis_schema(String postgis_schema) {
		this.postgis_schema = postgis_schema;
	}
	public String getPostgis_database() {
		return postgis_database;
	}
	public void setPostgis_database(String postgis_database) {
		this.postgis_database = postgis_database;
	}
	public String getPostgis_user() {
		return postgis_user;
	}
	public void setPostgis_user(String postgis_user) {
		this.postgis_user = postgis_user;
	}
	public String getPostgis_passwd() {
		return postgis_passwd;
	}
	public void setPostgis_passwd(String postgis_passwd) {
		this.postgis_passwd = postgis_passwd;
	}
	
	
}
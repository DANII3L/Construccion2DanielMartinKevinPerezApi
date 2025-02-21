package tdea.construccion2.app.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private CustomerDetailsService customerDetailsService;
	@Autowired
	private JwtFilter jwtFilter;
	
	@Bean
	public PasswordEncoder passwordEndcoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity
			.cors()
			.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
			.and()
			.csrf().disable()
			.authorizeHttpRequests()
			.requestMatchers("/Home/login").permitAll()
			.anyRequest()
			.authenticated()
			.and().exceptionHandling()
			.and().sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
			
		return httpSecurity.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	public CustomerDetailsService getCustomerDetailsService() {
		return customerDetailsService;
	}

	public void setCustomerDetailsService(CustomerDetailsService customerDetailsService) {
		this.customerDetailsService = customerDetailsService;
	}

	public JwtFilter getJwtFilter() {
		return jwtFilter;
	}

	public void setJwtFilter(JwtFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}
}

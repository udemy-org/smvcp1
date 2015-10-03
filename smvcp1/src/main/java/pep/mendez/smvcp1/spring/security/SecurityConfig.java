package pep.mendez.smvcp1.spring.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

import pep.mendez.smvcp1.spring.model.service.CustomUserDetailsService;

/*
 * Cross-Site Request Forgery (CSRF) 
 * is a type of attack that occurs when a malicious Web site, email, blog, 
 * instant message, or program causes a user's Web browser to perform an 
 * unwanted action on a trusted site for which the user is currently authenticated.
 * 
 * CSRF is an attack which forces an end user to execute unwanted actions 
 * on a web application in which he/she is currently authenticated.
 * 
 */
@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomUserDetailsService userDao;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.passwordEncoder(passwordEncoder);
//		 auth.userDetailsService(userDao).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.authorizeRequests()
				.antMatchers("/login", "/validate/**", "/register", "/help", "/about", "/resources/**", "/webjars/**")
				.permitAll()
				.antMatchers("/home")
				.hasAnyRole("USER", "ADMIN")
				.antMatchers("/admin", "/edit/**")
				.hasRole("ADMIN")
				// Security SpEL
				//.access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
				// .hasAnyRole("USER", "ADMIN")
				// appends ROLE_
				.anyRequest().authenticated().and().formLogin()
				.loginPage("/login").and().httpBasic()
				.and().exceptionHandling().accessDeniedPage("/403")
				.and().rememberMe()
				.tokenValiditySeconds(2419200).key("smvcp1").and()
				// .requiresChannel().antMatchers("/**").requiresSecure().and()
				.sessionManagement().maximumSessions(1);
	}

}

/*
 * in memory authentication is used for testing purposes
 */
// auth
// .inMemoryAuthentication()
// .withUser("user").password("password").roles("USER")
// .and()
// .withUser("admin").password("password").roles("USER", "ADMIN");

/*
 * real production jdbc data source
 */
// auth.jdbcAuthentication().dataSource(dataSource);

/*
 * real production jdbc encrypted data source
 */
// DI of AuthenticationManagerBuilder
// to avoid: No AuthenticationProvider found for
// org.springframework.security.authentication.UsernamePasswordAuthenticationToken

//
// // inMemoryAuthentication good for testing
//
// // auth.inMemoryAuthentication()
// // .withUser("user").password("password").roles("USER")
// // .and()
// // .withUser("admin").password("password").roles("USER", "ADMIN");
//
// // in real production you will use a data base
//

// @Override
// protected void configure(AuthenticationManagerBuilder auth)
// throws Exception {
//
// auth.jdbcAuthentication().dataSource(dataSource)
// .passwordEncoder(passwordEncoder);
//
// // solution for old style legacy data bases
// // auth.userDetailsService(userDao).passwordEncoder(passwordEncoder);
// }

// What is httpBasic() for?
// don't apply any authentication to the login page
// avoids the 'This webpage has a redirect loop' error
// a Controller is missing though

// This session has been expired (possibly due to multiple concurrent
// logins being attempted as the same user).
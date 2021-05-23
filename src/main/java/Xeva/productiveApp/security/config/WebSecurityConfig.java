package Xeva.productiveApp.security.config;

import Xeva.productiveApp.appUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().
                authorizeRequests().
                    antMatchers("/api/v*/registration/**").permitAll(). //Dostęp do strony rejestracji, bez logowania
                    antMatchers("/api/v*/login/**").permitAll(). //Dostęp do strony logowania, bez logowania
                    antMatchers("/api/v*/resetToken/**").permitAll(). //Dostep do resetu hasla bez logowania
                    antMatchers("/api/v*/newPassword/**").permitAll(). //Dostep do ustawienia nowego hasla bez logowania
                    antMatchers("/api/v*/task/**").permitAll().
                    antMatchers("/api/v*/tag/**").permitAll().
                    antMatchers("/api/v*/delegate/**").permitAll().
                    antMatchers("/api/v*/filterSettings/**").permitAll().
                    antMatchers("/api/v*/userImage/**").permitAll().
                    antMatchers("/api/v*/userData/**").permitAll().
                    antMatchers("/api/v*/account/**").permitAll().
                    antMatchers("/api/v*/localization/**").permitAll().
        anyRequest().
        authenticated().and()
        .formLogin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);

        return provider;

    }

}

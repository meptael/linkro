package me.meptael.linkro.account;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        return newAccount;
    }

    private Account saveNewAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .build();

        return accountRepository.save(account);
    }
    
    public void login(Account account) {
    	UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
    			new UserAccount(account),
    			account.getPassword(),
    			List.of(new SimpleGrantedAuthority("ROLE_USER")));
    	
    	SecurityContextHolder.getContext().setAuthentication(token);
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByEmail(username);
		
		if(account == null) {
			account = accountRepository.findByNickname(username);
		}
		
		if (account == null) {
            throw new UsernameNotFoundException(username);
        }
		
	    return new UserAccount(account);
	}

}

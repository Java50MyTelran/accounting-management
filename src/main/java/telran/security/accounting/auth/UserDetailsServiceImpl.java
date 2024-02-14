package telran.security.accounting.auth;

import java.util.Arrays;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.security.accounting.model.Account;
import telran.security.accounting.repo.AccountRepo;
@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
final AccountRepo accountRepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepo.findById(username)
				.orElseThrow(() -> new UsernameNotFoundException(""));
		String[] roles = Arrays.stream(account.getRoles())
				.map(role -> "ROLE_" + role).toArray(String[]::new);
		log.debug("username: {}, password: {}, roles: {}",
				account.getEmail(), account.getHashPassword(), Arrays.deepToString(roles));
		return new User(account.getEmail(), account.getHashPassword(),
				AuthorityUtils.createAuthorityList(roles));
	}

}

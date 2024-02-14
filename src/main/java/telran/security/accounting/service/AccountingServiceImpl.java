package telran.security.accounting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.security.accounting.dto.AccountDto;
import telran.security.accounting.model.Account;
import telran.security.accounting.repo.AccountRepo;

@Service
@Slf4j
public class AccountingServiceImpl implements AccountingService{
	@Autowired
AccountRepo accountRepo;

	@Override
	public AccountDto addAccount(AccountDto accountDto) {
		String email = accountDto.email();
		if(accountRepo.existsById(email)) {
			throw new IllegalStateException(String.format("account %s already exists", email));
		}
		Account account = accountRepo.save(Account.of(accountDto));
		log.debug("account {} has been saved", email);
		return account.build();
	}

	@Override
	public AccountDto removeAccount(String email) {
		Account account = accountRepo.findById(email)
				.orElseThrow(()->new NotFoundException(String.format("Account %s doesn't exist", email)));
		accountRepo.delete(account);
		log.debug("account {} has been removed", email);
		return account.build();
	}
	
}

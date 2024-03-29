package telran.security.accounting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.DeleteResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.security.accounting.dto.AccountDto;
import telran.security.accounting.exceptions.AccountNotFoundException;
import telran.security.accounting.exceptions.AccountStateException;
import telran.security.accounting.model.Account;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountingServiceImpl implements AccountingService{
	final MongoTemplate mongoTemplate;

	@Override
	public AccountDto addAccount(AccountDto accountDto) {
		String email = accountDto.email();
		Account account = null;
		try {
			account = mongoTemplate.insert(Account.of(accountDto));
		} catch (DuplicateKeyException e) {
			throw new AccountStateException(email);
		}
		log.debug("account {} has been saved",email);
		return account.build();
	}

	

	@Override
	public AccountDto removeAccount(String email) {
		Account account = mongoTemplate.findAndRemove(new Query(Criteria.where("email")
				.is(email)), Account.class);
		if(account == null) {
			throw new AccountNotFoundException(email);
		}
		log.debug("account {} has been removed", email);
		return account.build();
	}
	
}

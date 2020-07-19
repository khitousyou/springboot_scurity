 package jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jwt.dao.AppRoleRepository;
import jwt.dao.AppUserRepository;
import jwt.entities.AppRole;
import jwt.service.AccountService;
import jwt.service.IAccountService;

@SpringBootApplication
public class AuthentificationApplication implements CommandLineRunner {

	@Autowired
	private IAccountService accountService;
	
	public static void main(String[] args) {
		SpringApplication.run(AuthentificationApplication.class, args);
	}
	
	

	@Override
	public void run(String... args) throws Exception {
		accountService.save(new AppRole(null,"USER"));
		accountService.save(new AppRole(null,"ADMIN"));
		accountService.saveUser("user", "1234", "1234","USER");
		accountService.saveUser("admin", "1234", "1234","ADMIN");
		
	
	}

}

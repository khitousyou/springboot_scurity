package jwt.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jwt.dao.AppRoleRepository;
import jwt.dao.AppUserRepository;
import jwt.entities.AppRole;
import jwt.entities.AppUser;
@Service
@Transactional
public class AccountService implements IAccountService {

	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppRoleRepository appRoleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	 @Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	        return bCryptPasswordEncoder;
	    }

	@Override
	public AppUser saveUser(String username, String password, String confirmedPassword,String role) {
		AppUser appUser=this.appUserRepository.findByUsername(username);
		if(appUser!=null) throw new RuntimeException("User already exists !");
		if(!password.equals(confirmedPassword)) throw new RuntimeException("Pleas confirm your password");
		AppUser user=new AppUser();
		user.setUsername(username);
		user.setActivated(true);
		user.setPassword(bCryptPasswordEncoder.encode(password));
		appUserRepository.save(user);
		
		this.addRoleToUser(username, role);
		if(role.equals("ADMIN"))
			this.addRoleToUser(username, "USER");
		return user;
	}

	@Override
	public AppRole save(AppRole role) {
		AppRole appRole=appRoleRepository.save(role);
		return appRole;
	}

	@Override
	public AppUser loadUserByUserName(String username) {
		AppUser user=this.appUserRepository.findByUsername(username);
		return user;
	}

	@Override
	public void addRoleToUser(String username, String rolename) {
		AppRole appRole=appRoleRepository.findByRoleName(rolename);
		AppUser appUser=appUserRepository.findByUsername(username);
		appUser.getRoles().add(appRole);
	
		
	}

}

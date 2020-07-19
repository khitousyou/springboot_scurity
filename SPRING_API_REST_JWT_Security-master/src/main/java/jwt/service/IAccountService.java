package jwt.service;

import jwt.entities.AppRole;
import jwt.entities.AppUser;

public interface IAccountService {
	public AppUser saveUser(String username,String password,String confirmedPassword,String role);
	public AppRole save(AppRole role);
	public AppUser loadUserByUserName(String username);
	public void addRoleToUser(String username,String rolename);
}

package jwt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jwt.entities.AppUser;
import jwt.service.AccountService;
import lombok.Data;

@RestController
public class UserController {

	@Autowired
	AccountService accountService;
	@PostMapping("/register")
	public AppUser register(@RequestBody UserForm userForm)
	{
		
		return accountService.saveUser(userForm.getUsername(), userForm.getPassword(), userForm.getPassword(),userForm.getRole());
	}
}
@Data
class UserForm{
	private String username;
	private String password;
	private String confirmedPassword;
	private String role;
}

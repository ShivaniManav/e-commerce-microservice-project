package com.sm.iam.controller;

import com.sm.iam.entity.User;
import com.sm.iam.entity.UserAddress;
import com.sm.iam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://127.0.0.1:5501",allowedHeaders = "*")
@RestController
@RequestMapping("account")
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/details")
	public User getUser(@RequestParam String username) throws Exception {
		try {
			return userService.findByUserName(username);
		} catch (Exception e) {
			throw new Exception("No such user was found");
		}
	}
	
	@PostMapping("/details")
	public String updateDetails() throws Exception {
		return "";
	}
	
	@PostMapping("/address")
	public String addUserAddress(@RequestParam String username, @RequestBody UserAddress userAddress) {
		try {
			// User theUser = userService.findByUserName(username);
			// userAddress.setFkUser(theUser);
			userService.saveUserAddress(userAddress);
			return "Address saved successfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "bad request";
		}
		
	}
	
	@PostMapping("/address/update-default")
	public String updatedDefaultAddress(@RequestParam("user_id") int userId, @RequestParam("addr_id") int addrId) throws Exception {
		try {
			userService.updateDefaultAddress(userId, addrId);
			return "default address was updated successfully";
		} catch (Exception e) {
			throw new Exception("No such user was found");
		}
	}
	
}

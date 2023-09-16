package com.sm.iam.controller;


import com.sm.core.exception.SMServiceException;
import com.sm.core.util.CookieUtil;
import com.sm.core.util.JWTUtil;
import com.sm.iam.dto.response.AuthenticationResponse;
import com.sm.iam.entity.User;
import com.sm.iam.dto.request.PasswordResetRequest;
import com.sm.iam.dto.request.RegistrationRequest;
import com.sm.iam.service.MailService;
import com.sm.iam.service.OtpService;
import com.sm.iam.service.TokenService;
import com.sm.iam.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) throws Exception {
		Map<String, Object> response = new HashMap<>();
		try {
			User user = createUserModelFromRequest(registrationRequest);
			userService.save(user);
		} catch (Exception e) {
			throw new SMServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		response.put("message", "User Registered Successfully");
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/validate")
	public ResponseEntity<AuthenticationResponse> validateUser(HttpServletRequest request, HttpServletResponse response) {
		String username = (String) request.getAttribute("username");
		Set<GrantedAuthority> simpleGrantedAuthorities =
				(Set<GrantedAuthority>) request.getAttribute("authorities");
		return ResponseEntity.ok(AuthenticationResponse.builder().status(HttpStatus.OK.getReasonPhrase())
				.username(username).authorities(simpleGrantedAuthorities).isAuthenticated(true).build());
	}
	
	@PostMapping("/password-reset")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> passwordReset(@RequestBody PasswordResetRequest passwordResetRequest, HttpServletRequest request) throws Exception {
		Map<String, Object> responseEntityBody = new HashMap<>();
		String token = CookieUtil.getAccessTokenFromCookie(request.getCookies());
		String username = jwtUtil.getUsernameFromToken(token);
		User user = userService.findByUserName(username);
		if(passwordEncoder.matches(passwordResetRequest.getOldPassword(), user.getPassword())) {
			if(passwordResetRequest.getNewPassword().equals(passwordResetRequest.getConfirmNewPassword())) {
				user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
			} else {
				throw new Exception("New password and Confirm new password fields must match!");
			}
		} else {
			throw new Exception("Invalid current password provided!");
		}
		userService.save(user);
		responseEntityBody.put("message", "Password has been resetted successfully for " + user.getUsername());
		return new ResponseEntity<>(responseEntityBody, HttpStatus.OK);
	}
	
	@GetMapping("/forgot-password-email")
	@ResponseStatus(HttpStatus.OK)
	public void processForgotPasswordEmailForm(@RequestParam(name = "email") String email) throws Exception {
		User user = userService.findByEmail(email);
		if(user != null) {
			int otp = otpService.generateOTP(email);
			mailService.sendOtpEmail(String.valueOf(otp), email);
		} else {
			throw new Exception("User not found!!");
		}
	}
	
	@PostMapping("/validate-forgot-password-otp")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<?> processForgotPasswordOtpForm(@RequestBody Map<String, String> request) throws Exception {
		Map<String, Object> response = new HashMap<>();
		String clientOtp = String.valueOf(request.get("otp"));
		String serverOtp = null;
		if(Objects.nonNull(clientOtp) && !clientOtp.isEmpty()) {
			serverOtp = otpService.getOTP(request.get("email"), clientOtp);
		}
		if(Objects.isNull(serverOtp)) {
			throw new Exception("Invalid OTP!");
		}
		otpService.invalidateOTP(request.get("email"), clientOtp);
		String passwordResetToken = RandomString.make(30);
		tokenService.generatePasswordResetToken(request.get("email"), passwordResetToken);
		response.put("isOtpValid", true);
		response.put("passwordResetToken", passwordResetToken);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/forgot-password")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> processForgotPassword(@RequestBody Map<String, String> request, 
					@RequestParam(name = "passwordResetToken") String passwordResetToken) throws Exception {
		Map<String, Object> response = new HashMap<>();
		String newPwd = request.get("newPassword");
		String confNewPwd = request.get("confirmNewPassword");
		String email = tokenService.getEmailByPasswordResetToken(passwordResetToken);
		if(email == null) {
			throw new Exception("Invalid Password Reset Token!");
		}
		if(newPwd.equals(confNewPwd)) {
			User user = userService.findByEmail(email);
			user.setPassword(passwordEncoder.encode(newPwd));
			userService.save(user);
			tokenService.deletePasswordResetToken(passwordResetToken);
			response.put("message", "Password was reset successfully for " + user.getUsername());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			throw new Exception("Both password fields must match!");
		}
	}
	
	public User createUserModelFromRequest(RegistrationRequest registrationRequest) {
		return User.builder().username(registrationRequest.getUsername())
				.password(passwordEncoder.encode(registrationRequest.getPassword()))
				.firstName(registrationRequest.getFirstName()).lastName(registrationRequest.getLastName())
				.email(registrationRequest.getEmail()).mobile(registrationRequest.getMobile())
				.roles(registrationRequest.getRoles()).active(true).createdAt(new Date())
				.modifiedAt(new Date()).build();
	}
	
}

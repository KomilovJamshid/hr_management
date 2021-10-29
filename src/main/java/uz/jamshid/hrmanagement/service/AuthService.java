package uz.jamshid.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.jamshid.hrmanagement.entity.Role;
import uz.jamshid.hrmanagement.entity.User;
import uz.jamshid.hrmanagement.payload.ApiResponse;
import uz.jamshid.hrmanagement.payload.LoginDto;
import uz.jamshid.hrmanagement.payload.RegisterDto;
import uz.jamshid.hrmanagement.repository.RoleRepository;
import uz.jamshid.hrmanagement.repository.UserRepository;
import uz.jamshid.hrmanagement.security.JwtProvider;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse register(RegisterDto registerDto) {
        User user = new User();

        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail)
            return new ApiResponse("Email already exists", false);

        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setEmailCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Optional<Role> optionalRole = roleRepository.findById(registerDto.getRoleId());
        if (!optionalRole.isPresent())
            return new ApiResponse("Role doesn't found", false);
        user.setRole(optionalRole.get());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Role currentUserRole = currentUser.getRole();

        if (currentUserRole.getRoleName().name().equals("DIRECTOR")) {
            if (user.getRole().getRoleName().name().equals("MANAGER") || user.getRole().getRoleName().name().equals("HR_MANAGER")) {
                userRepository.save(user);
                sendEmail(user.getEmail(), user.getEmailCode());
                return new ApiResponse("Manager or HR-Manager saved successfully", true);
            }
            return new ApiResponse("Director is able to add only Manager and HR-Manager", false);
        }

        if (currentUserRole.getRoleName().name().equals("MANAGER") || currentUserRole.getRoleName().name().equals("HR_MANAGER")) {
            if (user.getRole().getRoleName().name().equals("EMPLOYEE")) {
                userRepository.save(user);
                sendEmail(user.getEmail(), user.getEmailCode());
                return new ApiResponse("Employee saved successfully", true);
            }
            return new ApiResponse("Manager or HR-Manager is able to add only Employees", false);
        }
        return new ApiResponse("You don't have privileges!", false);
    }

    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            User user = (User) authentication.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRole());
            return new ApiResponse("Token", true, token);
        } catch (BadCredentialsException e) {
            return new ApiResponse("Username or password incorrect", false);
        }
    }

    public boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("jamshid.kh2000@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Confirm your account");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail + "'>Confirm</a>");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Account confirmed", true);
        }
        return new ApiResponse("Account already confirmed", false);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }
}

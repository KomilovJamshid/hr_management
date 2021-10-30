package uz.jamshid.hrmanagement.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.jamshid.hrmanagement.entity.User;
import uz.jamshid.hrmanagement.entity.enums.RoleName;
import uz.jamshid.hrmanagement.repository.RoleRepository;
import uz.jamshid.hrmanagement.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @Value(value = "${spring.sql.init.mode}")
    private String initMode;

    @Override
    public void run(String... args) throws Exception {
        if (initMode.equals("always")) {
            User director = new User("Jamshid", "Komilov",
                    "jamshid.kh2000@gmail.com", passwordEncoder.encode("DIRECTOR"),
                    roleRepository.findByRoleName(RoleName.DIRECTOR));
            userRepository.save(director);
        }
    }
}

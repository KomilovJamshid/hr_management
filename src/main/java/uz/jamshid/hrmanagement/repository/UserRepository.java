package uz.jamshid.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.jamshid.hrmanagement.entity.Role;
import uz.jamshid.hrmanagement.entity.User;

import javax.validation.constraints.Email;
import java.util.*;

@RepositoryRestResource(path = "user")
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(@Email String email);

    Optional<User> findByEmailAndEmailCode(@Email String email, String emailCode);

    Set<User> findAllByRoleIn(Collection<Role> role);
}

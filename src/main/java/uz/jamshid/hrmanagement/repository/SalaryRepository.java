package uz.jamshid.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.jamshid.hrmanagement.entity.Salary;
import uz.jamshid.hrmanagement.entity.User;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(path = "salary")
public interface SalaryRepository extends JpaRepository<Salary, UUID> {
    List<Salary> findAllByEmployee(User employee);
}

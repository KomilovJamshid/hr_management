package uz.jamshid.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.jamshid.hrmanagement.entity.Month;

@RepositoryRestResource(path = "month")
public interface MonthRepository extends JpaRepository<Month, Integer> {
}

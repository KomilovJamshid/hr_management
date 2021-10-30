package uz.jamshid.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.jamshid.hrmanagement.entity.Tourniquet;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RepositoryRestResource(path = "tourniquet")
public interface TourniquetRepository extends JpaRepository<Tourniquet, UUID> {
    @Query("select tr from Tourniquet tr where tr.idCardOwner = :employeeId and (tr.time >= :begin or tr.time <= :end)")
    List<Tourniquet> findAllByIdCardOwnerAndEnterDateTimeAndExitDateTimeBefore(UUID employeeId, Date begin, Date end);
}

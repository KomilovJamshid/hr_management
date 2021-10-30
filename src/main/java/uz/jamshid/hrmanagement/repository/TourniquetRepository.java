package uz.jamshid.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.jamshid.hrmanagement.entity.Tourniquet;

import java.util.UUID;

@RepositoryRestResource(path = "tourniquet")
public interface TourniquetRepository extends JpaRepository<Tourniquet, UUID> {

}

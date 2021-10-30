package uz.jamshid.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.jamshid.hrmanagement.entity.Task;
import uz.jamshid.hrmanagement.entity.User;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(path = "task")
public interface TaskRepository extends JpaRepository<Task, UUID> {
    Optional<Task> findByIdAndTaskReceiver_Email(UUID id, @Email String taskReceiver_email);

    List<Task> findAllByTaskReceiver(User taskReceiver);
}

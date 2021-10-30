package uz.jamshid.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.jamshid.hrmanagement.entity.Task;
import uz.jamshid.hrmanagement.entity.User;
import uz.jamshid.hrmanagement.entity.enums.RoleName;
import uz.jamshid.hrmanagement.entity.enums.TaskStatus;
import uz.jamshid.hrmanagement.payload.ApiResponse;
import uz.jamshid.hrmanagement.payload.TaskDto;
import uz.jamshid.hrmanagement.repository.TaskRepository;
import uz.jamshid.hrmanagement.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JavaMailSender javaMailSender;

    public ApiResponse addTask(TaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setDeadline(taskDto.getDeadline());

        Optional<User> optionalUser = userRepository.findById(taskDto.getTaskReceiverId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Employee not found", false);
        User taskReceiver = optionalUser.get();
        RoleName taskReceiverRoleName = taskReceiver.getRole().getRoleName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleName().name().equals("DIRECTOR")) {
            if (taskReceiverRoleName.name().equals("MANAGER") || taskReceiverRoleName.name().equals("HR_MANAGER")) {
                task.setTaskReceiver(taskReceiver);
                task.setTaskStatus(TaskStatus.NEW);
                taskRepository.save(task);
                emailAboutTask(taskReceiver.getEmail(), task.getId());
            } else {
                return new ApiResponse("You don't have privileges", false);
            }
        }

        if (currentUser.getRole().getRoleName().name().equals("MANAGER") || currentUser.getRole().getRoleName().name().equals("HR_MANAGER")) {
            if (taskReceiverRoleName.name().equals("EMPLOYEE")) {
                task.setTaskReceiver(taskReceiver);
                task.setTaskStatus(TaskStatus.NEW);
                taskRepository.save(task);
                emailAboutTask(taskReceiver.getEmail(), task.getId());
            } else {
                return new ApiResponse("You don't have privileges", false);
            }
        }

        return new ApiResponse("Task assigned from " + currentUser.getRole().getRoleName().name() + " " +
                currentUser.getFirstName() + " " + currentUser.getLastName() + " to " +
                taskReceiverRoleName.name() + " " + taskReceiver.getFirstName() + " " +
                taskReceiver.getLastName(), true);
    }

    public boolean emailAboutTask(String sendingEmail, UUID taskId) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("jamshid.kh2000@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("New task assigned to you");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/getTask?taskId=" + taskId + "&email=" + sendingEmail + "'>Confirm new task</a>");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse getTask(String email, UUID taskId) {
        Optional<Task> optionalTask = taskRepository.findByIdAndTaskReceiver_Email(taskId, email);
        if (!optionalTask.isPresent())
            return new ApiResponse("Task is not assigned to this employee", false);
        Task task = optionalTask.get();
        task.setTaskStatus(TaskStatus.IN_PROCESS);
        taskRepository.save(task);
        return new ApiResponse("Task confirmed and deadline is " + task.getDeadline(), true);
    }

    public ApiResponse completeTask(UUID id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("Task not found", false);
        Task task = optionalTask.get();
        task.setTaskStatus(TaskStatus.COMPLETED);
        taskRepository.save(task);

        Optional<User> optionalUser = userRepository.findById(task.getTaskAssigner());
        if (!optionalUser.isPresent())
            return new ApiResponse("Employee not found", false);
        User user = optionalUser.get();
        emailAboutCompletedTask(user.getEmail(), task.getId());
        return null;
    }

    public boolean emailAboutCompletedTask(String sendingEmail, UUID taskId) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("jamshid.kh2000@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Task completed");
            mailMessage.setText(taskId + " task completed successfully");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

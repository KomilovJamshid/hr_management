package uz.jamshid.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.jamshid.hrmanagement.entity.Salary;
import uz.jamshid.hrmanagement.entity.Task;
import uz.jamshid.hrmanagement.entity.Tourniquet;
import uz.jamshid.hrmanagement.entity.User;
import uz.jamshid.hrmanagement.entity.enums.RoleName;
import uz.jamshid.hrmanagement.payload.ApiResponse;
import uz.jamshid.hrmanagement.payload.EmployeeDto;
import uz.jamshid.hrmanagement.repository.*;

import java.util.*;

@Service
public class EmployeeService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    SalaryRepository salaryRepository;
    @Autowired
    TourniquetRepository tourniquetRepository;
    @Autowired
    TaskRepository taskRepository;

    public ApiResponse getAllEmployees() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleName().name().equals("DIRECTOR")) {
            Set<User> employeeList = userRepository.findAllByRoleIn(Collections.singleton(roleRepository.findByRoleName(RoleName.EMPLOYEE)));
            Set<User> hrManagerList = userRepository.findAllByRoleIn(Collections.singleton(roleRepository.findByRoleName(RoleName.HR_MANAGER)));
            Set<User> managerList = userRepository.findAllByRoleIn(Collections.singleton(roleRepository.findByRoleName(RoleName.MANAGER)));
            return new ApiResponse("Employees : " + employeeList +
                    " HR Mangers: " + hrManagerList +
                    " Managers " + managerList, true);
        }

        if (currentUser.getRole().getRoleName().name().equals("HR_MANAGER")) {
            Set<User> employeeList = userRepository.findAllByRoleIn(Collections.singleton(roleRepository.findByRoleName(RoleName.EMPLOYEE)));
            return new ApiResponse("Employees : " + employeeList, true);
        }
        return new ApiResponse("You don't have privileges", false);
    }

    public ApiResponse getAllDataAboutOneEmployee(UUID employeeId, Date begin, Date end) {
        Optional<User> optionalUser = userRepository.findById(employeeId);
        if (!optionalUser.isPresent())
            return new ApiResponse("Employee not found", false);

        User user = optionalUser.get();
        if (user.getRole().getRoleName().name().equals("EMPLOYEE")) {
            List<Tourniquet> tourniquetList = tourniquetRepository.findAllByIdCardOwnerAndEnterDateTimeAndExitDateTimeBefore(employeeId, begin, end);
            List<Task> taskList = taskRepository.findAllByTaskReceiver(user);
            List<Salary> salaryList = salaryRepository.findAllByEmployee(user);
            EmployeeDto employeeDto = new EmployeeDto();
            employeeDto.setTourniquetList(tourniquetList);
            employeeDto.setTaskList(taskList);
            employeeDto.setSalaryList(salaryList);
            employeeDto.setFirstName(user.getFirstName());
            employeeDto.setLastName(user.getLastName());
            employeeDto.setEmail(user.getEmail());
            return new ApiResponse("All data of " + user.getRole().getRoleName().name(), true, employeeDto);
        }
        return new ApiResponse("You don't have privileges", false);
    }
}

package uz.jamshid.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.jamshid.hrmanagement.entity.Month;
import uz.jamshid.hrmanagement.entity.Salary;
import uz.jamshid.hrmanagement.entity.User;
import uz.jamshid.hrmanagement.payload.ApiResponse;
import uz.jamshid.hrmanagement.payload.SalaryDto;
import uz.jamshid.hrmanagement.repository.MonthRepository;
import uz.jamshid.hrmanagement.repository.SalaryRepository;
import uz.jamshid.hrmanagement.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SalaryService {
    @Autowired
    SalaryRepository salaryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MonthRepository monthRepository;

    public ApiResponse paySalary(SalaryDto salaryDto) {
        Optional<User> optionalUser = userRepository.findById(salaryDto.getEmployeeId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Employee not found", false);
        User employee = optionalUser.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole().getRoleName().name().equals("HR_MANAGER") || currentUser.getRole().getRoleName().name().equals("DIRECTOR")) {
            Salary salary = new Salary();
            salary.setEmployee(employee);
            salary.setAmount(salaryDto.getAmount());

            Optional<Month> optionalMonth = monthRepository.findById(salaryDto.getMonthId());
            if (!optionalMonth.isPresent())
                return new ApiResponse("Month not found", false);
            salary.setMonth(optionalMonth.get());
            salaryRepository.save(salary);
            return new ApiResponse("Salary successfully assigned", true);
        }
        return new ApiResponse("You don't have privileges", false);
    }

    public ApiResponse getSalary(UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent())
            return new ApiResponse("Employee not found", false);
        List<Salary> salaryList = salaryRepository.findAllByEmployee(optionalUser.get());
        return new ApiResponse("List of salary for particular employee", true, salaryList);
    }
}

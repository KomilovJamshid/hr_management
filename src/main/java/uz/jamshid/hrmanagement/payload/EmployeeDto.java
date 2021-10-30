package uz.jamshid.hrmanagement.payload;

import lombok.Data;
import uz.jamshid.hrmanagement.entity.Salary;
import uz.jamshid.hrmanagement.entity.Task;
import uz.jamshid.hrmanagement.entity.Tourniquet;

import java.util.List;

@Data
public class EmployeeDto {
    private String firstName;

    private String lastName;

    private String email;

    List<Tourniquet> tourniquetList;

    List<Task> taskList;

    List<Salary> salaryList;
}

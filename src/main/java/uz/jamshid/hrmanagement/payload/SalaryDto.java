package uz.jamshid.hrmanagement.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class SalaryDto {
    @NotNull
    private Double amount;

    @NotNull
    private UUID employeeId;

    @NotNull
    private Integer monthId;
}

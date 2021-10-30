package uz.jamshid.hrmanagement.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
public class TourniquetDto {
    @NotNull
    private String tourniquetStatus;

    @NotNull
    private Date time;

    @NotNull
    private UUID employeeId;
}

package uz.jamshid.hrmanagement.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
public class TaskDto {
    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Date deadline;

    @NotNull
    private UUID taskReceiverId;
}

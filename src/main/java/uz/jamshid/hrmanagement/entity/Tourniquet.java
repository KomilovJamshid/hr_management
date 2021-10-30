package uz.jamshid.hrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.jamshid.hrmanagement.entity.enums.TourniquetStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Tourniquet {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TourniquetStatus status = TourniquetStatus.OUT; //by default employee will be at outside of company

    private Date time;

    @CreatedBy
    private UUID idCardOwner;
}

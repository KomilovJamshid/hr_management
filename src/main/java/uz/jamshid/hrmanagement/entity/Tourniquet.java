package uz.jamshid.hrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.jamshid.hrmanagement.entity.enums.TourniquetStatus;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

    private TourniquetStatus status;

    private Date time;

    @CreatedBy
    private UUID idCardOwner;
}

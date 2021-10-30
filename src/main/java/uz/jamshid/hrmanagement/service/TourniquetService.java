package uz.jamshid.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.jamshid.hrmanagement.entity.Tourniquet;
import uz.jamshid.hrmanagement.entity.enums.TourniquetStatus;
import uz.jamshid.hrmanagement.payload.ApiResponse;
import uz.jamshid.hrmanagement.payload.TourniquetDto;
import uz.jamshid.hrmanagement.repository.TourniquetRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class TourniquetService {
    @Autowired
    TourniquetRepository tourniquetRepository;

    public ApiResponse tourniquetCheck(TourniquetDto tourniquetDto) {
        Tourniquet tourniquet = new Tourniquet();
        tourniquet.setTime(Timestamp.valueOf(LocalDateTime.now()));
        tourniquet.setIdCardOwner(tourniquetDto.getEmployeeId());

        if (tourniquetDto.getTourniquetStatus().equals("OUT")) {
            tourniquet.setStatus(TourniquetStatus.valueOf("IN"));
            tourniquetRepository.save(tourniquet);
            return new ApiResponse("Employee entered to work", true);
        } else if (tourniquetDto.getTourniquetStatus().equals("IN")) {
            tourniquet.setStatus(TourniquetStatus.valueOf("OUT"));
            tourniquetRepository.save(tourniquet);
            return new ApiResponse("Employee exited from work", true);
        } else {
            return new ApiResponse("Tourniquet error", false);
        }
    }
}

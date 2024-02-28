package fpt.fall23.onlearn.dto.profile;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class ProfileRequest {

    private Long id;

    private String avatar;

    private String phone;

    private String firstName;

    private String lastName;

    private String email;

    private String description;

    private String address;

    private LocalDate dateOfBirth;

    private Boolean status;
}

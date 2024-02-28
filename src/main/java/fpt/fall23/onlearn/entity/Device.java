package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.course.CourseView;
import fpt.fall23.onlearn.dto.device.DeviceView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "device")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Device {

    @Id
    @JsonView(DeviceView.class)
    private String token;

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "account_id")
    private Account account;
}

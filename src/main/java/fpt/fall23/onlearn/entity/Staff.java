package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.dto.subject.SubjectView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "staff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({SubjectView.class})
    private Long id;

    private String staffNumber;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Account.class)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "staff")
    @JsonIgnore
    private Set<Subject> subjects;
    
    private String bankName;
    private String cardNumber;
    private String cardHolderName;

}

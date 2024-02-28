
package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.account.AccountView;
import fpt.fall23.onlearn.dto.course.CourseView;
import fpt.fall23.onlearn.dto.enroll.EnrollView;
import fpt.fall23.onlearn.dto.feedback.FeedbackView;
import fpt.fall23.onlearn.dto.paymenthistory.PaymentHistoryView;
import fpt.fall23.onlearn.dto.paymentmethods.PaymentMethodsView;
import fpt.fall23.onlearn.dto.quiz.ResultQuizView;
import fpt.fall23.onlearn.dto.report.ReportView;
import fpt.fall23.onlearn.dto.wallet.WalletView;
import fpt.fall23.onlearn.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = Account.COLLECTION_NAME)
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Account extends BaseEntity implements UserDetails {
    public static final String COLLECTION_NAME = "account";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({PaymentMethodsView.class, WalletView.class, PaymentHistoryView.class, AccountView.class,ReportView.class})
    private Long id;

    @Column(unique = true)
    @JsonView({FeedbackView.class, ResultQuizView.class, PaymentHistoryView.class, ResultQuizView.class, EnrollView.class})
    private String username;

    private String password;

    private Role role;

    private Boolean active = true;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonView({CourseView.class, EnrollView.class, ReportView.class})
    private Profile profile;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        return authorities;
    }

    @PreRemove
    private void preRemove() {

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return username;
    }
}

package fpt.fall23.onlearn.dto.account.request;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class CreateAccountRequest {
 private String username;
 private String password;
 private String email;
 private String firstName;
 private String lastName;
 private String phone;
}

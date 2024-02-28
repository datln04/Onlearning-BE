package fpt.fall23.onlearn.dto.auth.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleLoginRequest {
    private String idToken;
}

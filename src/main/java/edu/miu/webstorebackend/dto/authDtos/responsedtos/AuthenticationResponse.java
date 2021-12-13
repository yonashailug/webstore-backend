package edu.miu.webstorebackend.dto.authDtos.responsedtos;

import edu.miu.webstorebackend.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private UserDto user;
    private String type ;
    private String refreshToken;

}

package edu.miu.webstorebackend.dto;

import edu.miu.webstorebackend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDto {
    private long id;
    private String username;
    private String name;
    private String email;
    private List<String> roles;

}

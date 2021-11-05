package cn.dustlight.auth.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleClient extends DefaultClient implements RoleClient {

    private int count;
}

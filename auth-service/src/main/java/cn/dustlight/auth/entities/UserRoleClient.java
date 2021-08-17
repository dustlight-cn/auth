package cn.dustlight.auth.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleClient extends DefaultClient implements RoleClient {

    /**
     * 用户在该应用下的角色数量。
     */
    private int count;
}

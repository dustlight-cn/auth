package cn.dustlight.auth.entities;

public interface RoleClient extends Client {

    /**
     * 对象在此应用的角色数
     *
     * @return
     */
    int getCount();

}

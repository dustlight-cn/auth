package cn.dustlight.uim.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Set;

/**
 * User Details Interface
 */
public interface IUserDetails extends UserDetails {

    /**
     * Get user's ID;
     *
     * @return user ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    long getUid();

    /**
     * Get user's E-mail
     *
     * @return user's E-mail
     */
    String getEmail();

    /**
     * Get user's phone
     *
     * @return user's phone
     */
    String getPhone();

    /**
     * Get user's gender
     *
     * @return user's gender
     */
    int getGender();

    /**
     * Get user's role
     *
     * @return user's role
     */
    Long getRole();

    String getRoleName();

    String getRoleDescription();

    /**
     * Get user details creation time
     *
     * @return creation time
     */
    Date getCreatedAt();

    /**
     * Get user details update time
     *
     * @return update time
     */
    Date getUpdatedAt();

    Set<String> getAuthoritiesString();
}

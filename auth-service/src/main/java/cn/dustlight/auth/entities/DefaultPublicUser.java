package cn.dustlight.auth.entities;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PublicUser")
public class DefaultPublicUser extends DefaultUser implements PublicUser {
}

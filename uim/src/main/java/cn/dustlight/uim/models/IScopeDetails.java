package cn.dustlight.uim.models;

import java.io.Serializable;
import java.util.Date;

public interface IScopeDetails extends Serializable {

    Long getId();

    String getName();

    String getDescription();

    Date getCreatedAt();

    Date getUpdatedAt();
}

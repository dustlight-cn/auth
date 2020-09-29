package cn.dustlight.oauth2.uim.entities.v1.scopes;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "Scope")
public class DefaultScope implements Scope {

    private Long sid;
    private String name, subtitle, description;
    private Date createdAt, updatedAt;

    @Override
    public Long getSid() {
        return sid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSubtitle() {
        return subtitle;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

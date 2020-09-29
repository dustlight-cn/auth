package cn.dustlight.oauth2.uim.entities.v1.scopes;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "ClientScope")
public class DefaultClientScope implements ClientScope {

    private Long cid, sid;
    private String name, subtitle, description;
    private boolean autoApprove;
    private Date createdAt, updatedAt;

    @Override
    public Long getCid() {
        return cid;
    }

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
    public boolean isAutoApprove() {
        return autoApprove;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setCid(Long cid) {
        this.cid = cid;
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

    public void setAutoApprove(boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

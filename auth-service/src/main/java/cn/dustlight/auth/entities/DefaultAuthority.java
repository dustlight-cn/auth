package cn.dustlight.auth.entities;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "Authority")
public class DefaultAuthority implements Authority {

    private Long aid;
    private String authorityName, authorityDescription, cid;
    private Date createdAt, updatedAt;

    @Override
    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    @Override
    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    @Override
    public String getAuthorityDescription() {
        return authorityDescription;
    }

    @Override
    public String getCid() {
        return cid;
    }

    public void setAuthorityDescription(String authorityDescription) {
        this.authorityDescription = authorityDescription;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}

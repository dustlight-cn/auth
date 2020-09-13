package cn.dustlight.oauth2.uim.entities;

public class GrantType implements IGrantType {

    private Long id;
    private String name;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}

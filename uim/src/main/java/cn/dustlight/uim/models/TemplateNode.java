package cn.dustlight.uim.models;

public class TemplateNode {

    public String name;
    public String text;

    public TemplateNode() {

    }

    public TemplateNode(String name, String text) {
        setName(name);
        setText(text);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TemplateNode{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

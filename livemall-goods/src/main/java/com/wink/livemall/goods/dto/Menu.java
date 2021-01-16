package com.wink.livemall.goods.dto;


import java.io.Serializable;
import java.util.List;

public class Menu implements Serializable {
    private int id;
    private int parent_id;
    private String name;
    private String pic;
    private String home_pic;
    private List<Menu> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getHome_pic() {
        return home_pic;
    }

    public void setHome_pic(String home_pic) {
        this.home_pic = home_pic;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

}

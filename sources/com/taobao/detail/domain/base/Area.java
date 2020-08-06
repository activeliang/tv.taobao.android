package com.taobao.detail.domain.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Area implements Serializable {
    private String areaId;
    private List<Area> areas;
    private String name;

    public Area() {
    }

    public Area(String name2, String areaId2) {
        this.name = name2;
        this.areaId = areaId2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getAreaId() {
        return this.areaId;
    }

    public void setAreaId(String areaId2) {
        this.areaId = areaId2;
    }

    public List<Area> getAreas() {
        return this.areas;
    }

    public void setAreas(List<Area> areas2) {
        this.areas = areas2;
    }

    public void addSubArea(Area area) {
        if (getAreas() == null) {
            this.areas = new ArrayList();
        }
        this.areas.add(area);
    }
}

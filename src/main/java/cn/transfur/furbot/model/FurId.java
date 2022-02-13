package cn.transfur.furbot.model;

import java.util.List;

/**
 * Date: 2022/2/9
 * Author: Jmeow
 */
public class FurId {
    private String name;
    private List<Integer> fids;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFids(List<Integer> fids) {
        this.fids = fids;
    }

    public List<Integer> getFids() {
        return fids;
    }
}
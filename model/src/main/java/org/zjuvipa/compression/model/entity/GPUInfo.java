package org.zjuvipa.compression.model.entity;

import lombok.Data;

@Data
public class GPUInfo{
    private String name;
    private Integer tot_memory;
    private Integer remain_memory;

    public GPUInfo(String name, Integer tot_memory, Integer remain_memory){
        this.name = name;
        this.tot_memory = tot_memory;
        this.remain_memory = remain_memory;
    }
}
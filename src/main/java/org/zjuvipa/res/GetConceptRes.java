package org.zjuvipa.res;

import lombok.Data;

import java.util.List;

@Data
public class GetConceptRes {
    private String conceptName;

    private List<String> conceptAddress;
}

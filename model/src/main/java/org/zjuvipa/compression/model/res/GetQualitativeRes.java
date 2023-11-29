package org.zjuvipa.compression.model.res;

import lombok.Data;
import org.zjuvipa.compression.model.info.qualitativeInfo;

import java.util.List;

@Data
public class GetQualitativeRes {
    private List<qualitativeInfo> qualitativeinfo;
}

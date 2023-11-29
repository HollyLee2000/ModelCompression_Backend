package org.zjuvipa.compression.model.req;

import lombok.Data;

import java.util.List;

@Data
public class DeletePicturesReq {
    private List<Integer> pictureids;
}

package org.zjuvipa.req;

import lombok.Data;

import java.util.List;

@Data
public class DeletePicturesReq {
    private List<Integer> pictureids;
}

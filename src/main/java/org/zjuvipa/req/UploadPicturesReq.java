package org.zjuvipa.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Data
public class UploadPicturesReq {

//    private MultipartFile[] pictures;

    private int datasetId;

}

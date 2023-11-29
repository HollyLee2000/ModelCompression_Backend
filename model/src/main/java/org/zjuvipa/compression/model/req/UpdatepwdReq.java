package org.zjuvipa.compression.model.req;

import lombok.Data;
import lombok.NonNull;

@Data
public class UpdatepwdReq {
    @NonNull
    private String username;

    @NonNull
    private String newPassword;
}

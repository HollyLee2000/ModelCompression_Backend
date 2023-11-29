package org.zjuvipa.compression.backend.service;

import org.zjuvipa.compression.model.entity.Algorithm;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zjuvipa.compression.model.info.AlgorithmInfo;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
public interface IAlgorithmService extends IService<Algorithm> {
    public List<AlgorithmInfo> searchAlgorithm();
    public List<AlgorithmInfo> findAlgoByName(String algorithmName);
    public boolean uploadAlgorithm(String username, String name, Double score, String institute, Integer ranking, String morfPath,
                                   String lerfPath, String pythonPath, String email, String info, String dateTime, String status);
    public boolean algorithmReject(Integer algorithmId);
    public boolean algorithmApprove(Integer algorithmId);
}

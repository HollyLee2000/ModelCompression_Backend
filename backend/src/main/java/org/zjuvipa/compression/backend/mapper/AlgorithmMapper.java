package org.zjuvipa.compression.backend.mapper;

import org.apache.ibatis.annotations.Param;
import org.zjuvipa.compression.model.entity.Algorithm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.zjuvipa.compression.model.entity.User;
import org.zjuvipa.compression.model.info.AlgorithmInfo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
public interface AlgorithmMapper extends BaseMapper<Algorithm> {

    public List<Algorithm> findAlgoByName(String algorithmName);

    public List<Algorithm> searchAlgorithm();

    public boolean uploadAlgorithm(@Param("username") String username, @Param("name") String name, @Param("score") Double score,
                                   @Param("institute") String institute, @Param("ranking") Integer ranking, @Param("morfPath") String morfPath,
                                   @Param("lerfPath") String lerfPath, @Param("pythonPath") String pythonPath, @Param("email") String email,
                                   @Param("info") String info, @Param("dateTime") String dateTime, @Param("status") String status);

    public boolean algorithmReject(@Param("algorithmId") Integer algorithmId);

    public boolean algorithmApprove(@Param("algorithmId") Integer algorithmId);



//    String username, String name, Double score, String institute, Integer rank, String morfPath,
//    String lerfPath, String pythonPath, String email, String info
//
//    @Param("url") String url, @Param("name") String name, @Param("datasetId") int datasetId
//
//    uploadAlgorithm

}

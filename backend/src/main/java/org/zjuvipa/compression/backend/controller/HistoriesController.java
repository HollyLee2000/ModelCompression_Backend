package org.zjuvipa.compression.backend.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

//import org.zjuvipa.compression.model.info.UserInfo;
import org.zjuvipa.compression.common.util.ResultBean;
import org.zjuvipa.compression.model.req.GetUserHistoriesReq;
import org.zjuvipa.compression.model.res.GetUserHistoriesRes;
import org.zjuvipa.compression.backend.service.IAlgorithmService;
import org.zjuvipa.compression.backend.service.IHistoriesService;
import org.zjuvipa.compression.backend.service.IHistoryService;
import org.zjuvipa.compression.model.res.FindHistoryRes;
import org.zjuvipa.compression.model.info.*;
import org.zjuvipa.compression.model.req.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author panyan
 * @since 2022-09-22
 */
@RestController
@RequestMapping("/histories")
public class HistoriesController {

    @Autowired
    IHistoriesService iHistoriesService;

    @Autowired
    IHistoryService iHistoryService;

    @Autowired
    IAlgorithmService iAlgorithmService;

    @Resource
    private HttpServletResponse response;

    @Resource
    private HttpServletRequest request;

    @CrossOrigin
    @PostMapping("/getUserHistories")
    public ResultBean<GetUserHistoriesRes> getUserHistories(@RequestBody GetUserHistoriesReq req) {
        ResultBean<GetUserHistoriesRes> result = new ResultBean<>();
        GetUserHistoriesRes res = new GetUserHistoriesRes();
        res.setHistoriesInfos(iHistoriesService.getUserHistories(req.getUsername()));
        return result;
    }

    @CrossOrigin
    @ApiOperation("展示所有历史记录")
    @PostMapping("findHistoryByUser")
    public ResultBean<FindHistoryRes> findHistoryByUser(@RequestBody FindHistoriesByUserReq req, @CookieValue("userTicket")String ticket) {
        HttpSession httpSession = request.getSession();
        ResultBean<FindHistoryRes> result = new ResultBean<>();
        UserInfo userInfo = (UserInfo)httpSession.getAttribute(ticket);
        if(userInfo == null || !StringUtils.hasText(ticket)) {
            System.out.println("用户未登录，请跳转login页面");
            result.setMsg("用户未登录，请跳转login页面");
            result.setCode(ResultBean.NO_PERMISSION);
            result.setData(null);
            return result;
        }else{
            System.out.println("用户已登录，可以进行查询");
        }
//        if(userInfo.getAuthority() > 0 && !req.getUsername().equals(userInfo.getUsername())){//非管理员无权限查看别的用户的历史记录
//            System.out.println("无权限查看该用户历史记录！");
//            result.setMsg("无权限查看该用户历史记录！");
//            result.setCode(ResultBean.FAIL);
//            result.setData(null);
//            return result;
//        }
        FindHistoryRes histories = new FindHistoryRes();
        histories.setHistoryInfos(iHistoryService.findHistoriesByUser(req.getUsername()));
        if(histories.getHistoryInfos() != null) {
            result.setMsg("查询成功！共"+histories.getHistoryInfos().size()+"条记录");
            result.setData(histories);
        }
        else {
            result.setMsg("查询失败！共0条记录");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
        }
        return result;
    }


}

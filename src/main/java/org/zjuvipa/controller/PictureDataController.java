package org.zjuvipa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.zjuvipa.req.*;
import org.zjuvipa.res.DeletePicturesRes;
import org.zjuvipa.res.GetPicturesRes;
import org.zjuvipa.res.UploadPicturesRes;
import org.zjuvipa.service.IPictureDataService;
import org.zjuvipa.util.ResultBean;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
@Api(tags = "图像数据")
@RestController
@RequestMapping("/data")
public class PictureDataController {

    @Autowired
    IPictureDataService iPictureDataService;

    @Resource
    private HttpServletResponse response;

    @Resource
    private HttpServletRequest request;


    @CrossOrigin
    @ApiOperation("删除图像")
    @PostMapping("/deletepictures")
    public ResultBean<DeletePicturesRes> deletePictures(@RequestBody DeletePicturesReq req) {
        ResultBean<DeletePicturesRes> result = new ResultBean<>();
        DeletePicturesRes res = new DeletePicturesRes();
        iPictureDataService.deletePictures(req.getPictureids());
        result.setMsg("删除成功");
        result.setData(res);
        return result;
    }

    public String upload(MultipartFile file) {
        System.out.println("file.getSize(): " +  file.getSize());
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1,
                file.getOriginalFilename().length());
        String savePath = "/nfs/lhl/DuansBenchmark/userImg/";
        // String savePath = "C:\\OIDPL\\test_user_imgs\\";
        File savePathFile = new File(savePath);
        if (!savePathFile.exists()){
            savePathFile.mkdir();
        }
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
        try {
            file.transferTo(new File(savePath + filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("savePath + filename: " +  savePath + filename);

        return "10.214.242.155:7668/MetauserImg/"+filename;
        // return "192.168.2.55:7667/img/"+filename;
    }

    @CrossOrigin
    @ApiOperation("上传图像")
    @PostMapping("/uploadPictures")
    public ResultBean<UploadPicturesRes> uploadPictures(@RequestParam() Integer datasetId, @RequestParam(name = "pictures") MultipartFile[] pictures) throws IOException {
        System.out.println("datasetId: "+ datasetId);
        System.out.println("pictures: "+ pictures);
        ResultBean<UploadPicturesRes> result = new ResultBean<>();
        UploadPicturesRes res = new UploadPicturesRes();
        List<String> savePaths = new ArrayList<>();
        List<String> names = new ArrayList<>();
//        for (MultipartFile f : pictures) {
//            String s = upload(f);
//            savePaths.add(s);
//            names.add(f.getName());
//        }
        String s = upload(pictures[0]);
        System.out.println("s!!!: " + s);
        savePaths.add(s);
        names.add(pictures[0].getName());
        res.setSucceed(true);
        res.setImgpath(s);
        result.setData(res);
        result.setMsg("上传成功");
        return result;
    }

    public String uploadCsv(MultipartFile file) {
        System.out.println("file.getSize(): " +  file.getSize());
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1,
                file.getOriginalFilename().length());
        String savePath = "/nfs3-p1/duanjr/meta-score-attribution-hollylee/data/user_upload/";
        // String savePath = "C:\\OIDPL\\test_user_imgs\\";
        File savePathFile = new File(savePath);
        if (!savePathFile.exists()){
            savePathFile.mkdir();
        }
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
        try {
            file.transferTo(new File(savePath + filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("savePath + filename: " +  savePath + filename);

        return "10.214.242.155:7668/WorkSpace/data/user_upload/"+filename;
        // return "192.168.2.55:7667/img/"+filename;
    }

    @CrossOrigin
    @ApiOperation("上传CSV")
    @PostMapping("/uploadCsvs")
    public ResultBean<UploadPicturesRes> uploadCsvs(@RequestParam() Integer datasetId, @RequestParam(name = "pictures") MultipartFile[] pictures) throws IOException {
        System.out.println("datasetId: "+ datasetId);
        System.out.println("csvs: "+ pictures);
        ResultBean<UploadPicturesRes> result = new ResultBean<>();
        UploadPicturesRes res = new UploadPicturesRes();
        List<String> savePaths = new ArrayList<>();
        List<String> names = new ArrayList<>();
//        for (MultipartFile f : pictures) {
//            String s = upload(f);
//            savePaths.add(s);
//            names.add(f.getName());
//        }
        String s = uploadCsv(pictures[0]);
        System.out.println("csv: " + s);
        savePaths.add(s);
        names.add(pictures[0].getName());
        res.setSucceed(true);
        res.setImgpath(s);
        result.setData(res);
        result.setMsg("csv上传成功");
        return result;
    }

    @CrossOrigin
    @ApiOperation("展示图像")
    @PostMapping("/getPictures")
    public ResultBean<GetPicturesRes> getPictures(@RequestBody GetPicturesReq req) {
        ResultBean<GetPicturesRes> result = new ResultBean<>();
        GetPicturesRes res = new GetPicturesRes();
        res.setPictures(iPictureDataService.getPictures(req.getDatasetId()));
        result.setData(res);
        result.setMsg("查询成功");
        return result;
    }



}
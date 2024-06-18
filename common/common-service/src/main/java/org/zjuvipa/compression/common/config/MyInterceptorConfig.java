package org.zjuvipa.compression.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file:/nfs/lhl/images/");
        registry.addResourceHandler("/MetauserImg/**").addResourceLocations("file:/nfs/lhl/DuansBenchmark/userImg/");
        registry.addResourceHandler("/3dmodel/**").addResourceLocations("file:/home/py/models/");
        registry.addResourceHandler("/cocoimg/**").addResourceLocations("file:/nfs/lhl/datasets/COCO2017/images/train/");
        registry.addResourceHandler("/originalCoCoimg/**").addResourceLocations("file:/nfs/lhl/datasets/COCO2017/images/train/");
        registry.addResourceHandler("/originalVOC/**").addResourceLocations("file:/nfs/lhl/datasets/VOCdevkit/VOC2012/JPEGImages/");
        registry.addResourceHandler("/originalImgNet/**").addResourceLocations("file:/nfs/lhl/datasets/ILSVRC2012/train/");
        registry.addResourceHandler("/Concept/**").addResourceLocations("file:/nfs/lhl/OIPDL_codes/TCAV/lhl_concepts/");
        registry.addResourceHandler("/CUB/**").addResourceLocations("file:/nfs/lhl/datasets/CUB_200_2011/images/");
        registry.addResourceHandler("/CARS/**").addResourceLocations("file:/nfs/lhl/datasets/cars/cars_train/");
        registry.addResourceHandler("/Cifar/**").addResourceLocations("file:/nfs/lhl/datasets/cifar/cifar-10-batches-py/train/");
        registry.addResourceHandler("/VOC/**").addResourceLocations("file:/nfs/lhl/datasets/VOCdevkit/VOC2012/JPEGImages/");
        registry.addResourceHandler("/Prototype/**").addResourceLocations("file:/nfs/lhl/OIPDL_codes/CUB_CARS/tests/");
        registry.addResourceHandler("/NWPU/**").addResourceLocations("file:/nfs/lhl/datasets/NWPU-RESISC45_dataset/train/");
        registry.addResourceHandler("/Food101/**").addResourceLocations("file:/nfs/lhl/datasets/food-101/food-101/train/");
        registry.addResourceHandler("/Place365/**").addResourceLocations("file:/nfs3-p2/duanjr/places365_standard/train/");
        registry.addResourceHandler("/WorkSpace/**").addResourceLocations("file:/nfs/lhl/Torch-Pruning/");
        registry.addResourceHandler("/Checkpoints/**").addResourceLocations("file:/nfs3-p1/djr/externalUSB/consistency/checkpoints/checkpoints/");
        //data/user_upload
        registry.addResourceHandler("/ckpt/**").addResourceLocations("file:/home/lhl/.cache/torch/hub/checkpoints/");

    }
}

///nfs/lhl/datasets/food-101/food-101/train

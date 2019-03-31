package com.netease.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netease.constant.ExceptionMsg;
import com.netease.exception.ParamException;
import com.netease.exception.PermissionException;
import com.netease.model.Goods;
import com.netease.model.User;
import com.netease.service.GoodsService;
import com.netease.service.SoldGoodsService;
import com.netease.util.JsonUtils;
import com.netease.vo.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/goods")
@PropertySource(value = {"classpath:server/ImageServer.properties"})
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    @Resource
    private SoldGoodsService soldGoodsService;

    @Value("${ImgServerDir}")
    private String realPath;

    @RequestMapping("/remove")
    public String removeGoods(HttpServletRequest request, @RequestParam("goodsId") Integer goodsId) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null || user.getId() == null) {
            throw new PermissionException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }

        if (goodsId == null) {
            throw new ParamException(ExceptionMsg.ILLEGAL_PARAMETER_EXCEPTION);
        }
        goodsService.remove(goodsId);

        return "redirect:/goods/list.page";
    }

    @RequestMapping("/bought/list.page")
    public String showBoughtGoodsListPage(HttpServletRequest request, Model model) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null || user.getId() == null) {
            throw new PermissionException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }

        List<Integer> soldGoodsIdList = soldGoodsService.getSoldGoodsIdListByUserId(user.getId());
        HashSet<Integer> noRepeatGoodsIdSet = new HashSet<>(soldGoodsIdList);
        List<Goods> goodsList = new ArrayList<>();

        for (Integer goodsId : noRepeatGoodsIdSet) {
            Goods goods = goodsService.getGoodsById(goodsId);
            goods.setFlag(1);
            goodsList.add(goods);
        }
        model.addAttribute("goodsList", goodsList);

        return "goods_list";
    }

    @RequestMapping("/sold/list.page")
    public String showSoldGoodsListPage(HttpServletRequest request, Model model) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null || user.getId() == null) {
            throw new PermissionException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }

        List<Goods> goodsList = goodsService.getSoldGoodsByUserId(user.getId());
        for (Goods goods : goodsList) {
            goods.setFlag(2);
        }

        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @RequestMapping("/api/upload")
    @ResponseBody
    public String uploadGoodsImage(@RequestParam(value = "file", required = false) CommonsMultipartFile file, HttpServletRequest request) throws JsonProcessingException {


        User user = (User) request.getSession().getAttribute("user");
        if (user == null || user.getId() == null) {
            throw new PermissionException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }

        // 自定义的文件名称
        String trueFileName = UUID.randomUUID().toString().replace("-", "") + "_" + file.getOriginalFilename();

        // 设置上传图片文件的路径
        String uploadPath = realPath + "/products/";
        String completePath = uploadPath + trueFileName;

        try {
            File uploadDir = new File(uploadPath);
            //若待存放图片目录不存在，则创建该目录
            if (!uploadDir.exists()) {
                if (uploadDir.mkdir()) {
                    System.out.println(uploadPath + "目录创建成功");
                } else {
                    System.out.println(uploadPath + "目录创建失败");
                }
            }
            // 转存文件到指定的路径
            File newFile = new File(completePath);
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Response response = new Response();
        String mapPath = "/images/" + "products/" + trueFileName;
        response.setResult(mapPath);
        return JsonUtils.obj2String(response);
    }

    @RequestMapping("/edit")
    public String editGoods(Goods goods, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null || user.getId() == null) {
            throw new PermissionException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }
        if (goods == null || goods.getId() == null) {
            throw new ParamException(ExceptionMsg.ILLEGAL_PARAMETER_EXCEPTION);
        }
        goodsService.updateGoods(goods);
        return "redirect:/goods/info.page?goodsId=" + goods.getId();
    }

    @RequestMapping("/edit.page")
    public String showGoodsEditPage(@RequestParam("goodsId") Integer goodsId, Model model) {

        if (goodsId == null) {
            throw new ParamException(ExceptionMsg.ILLEGAL_PARAMETER_EXCEPTION);
        }

        Goods goods = goodsService.getGoodsById(goodsId);
        model.addAttribute("goods", goods);
        return "goods_edit";
    }

    @RequestMapping("/release.page")
    public String showGoodsReleasePage() {
        return "goods_release";
    }

    @RequestMapping("/release")
    public String releaseGoods(Goods goods, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null || user.getId() == null) {
            throw new PermissionException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }

        if (goods == null) {
            throw new ParamException(ExceptionMsg.ILLEGAL_PARAMETER_EXCEPTION);
        }

        goods.setUserId(user.getId());
        goodsService.insertGoods(goods);
        return "redirect:/goods/list.page";
    }

    @RequestMapping("/list.page")
    public String showGoodsListPage(HttpServletRequest request, Model model) {

        List<Goods> goodsList = goodsService.getGoodsListWithFlag(request);
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @RequestMapping("/info.page")
    public String showGoodsInfoPage(@RequestParam("goodsId") Integer goodsId, Model model, HttpServletRequest request) {

        if (goodsId == null) {
            throw new ParamException(ExceptionMsg.ILLEGAL_PARAMETER_EXCEPTION);
        }

        User user = (User) request.getSession().getAttribute("user");
        Goods goods = goodsService.getGoodsById(goodsId);
        model.addAttribute("goods", goods);

        if (user != null && user.getId() != null) {
            int soldQuantity = goodsService.getSoldQuantityById(goodsId);
            model.addAttribute("soldQuantity", soldQuantity);
        }
        return "goods_info";
    }
}

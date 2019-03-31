package com.netease.controller;

import com.netease.constant.ExceptionMsg;
import com.netease.constant.StandardCode;
import com.netease.exception.ParamException;
import com.netease.exception.PermissionException;
import com.netease.model.User;
import com.netease.service.CartService;
import com.netease.vo.CartItemVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @RequestMapping("/remove")
    public String removeFromCart(@RequestParam("id") Integer cartItemId, HttpServletRequest request) {

        User user= (User) request.getSession().getAttribute("user");
        if (cartItemId == null) {
            throw new ParamException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }

        if(user == null || user.getId() == null){
            throw new PermissionException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }

        int code = cartService.remove(cartItemId, user);
        //code 为0 表示删除失败，1表示删除成功
        if (code == StandardCode.QUERY_FAILED_CODE) {
            throw new ParamException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }
        return "redirect:/cart/list.page";
    }

    @RequestMapping("/clear")
    public String clearCart(HttpServletRequest request) {

        User user= (User) request.getSession().getAttribute("user");
        if(user == null || user.getId() == null){
            throw new PermissionException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }

        cartService.clear(user);
        return "redirect:/cart/list.page";
    }

    @RequestMapping("/add")
    public String addGoodsToCart(Integer goodsId, Integer quantity, HttpServletRequest request) {

        User user= (User) request.getSession().getAttribute("user");
        if(user == null || user.getId() == null){
            throw new PermissionException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }

        if (goodsId == null || quantity == null || quantity <= 0) {
            throw new ParamException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }

        cartService.addGoods(goodsId, quantity, user);
        return "redirect:/cart/list.page";
    }

    @RequestMapping("/list.page")
    public String showCartItemList(HttpServletRequest request, Model model) {

        User user= (User) request.getSession().getAttribute("user");
        String refererURL = request.getHeader("REFERER");

        if(user == null || user.getId() == null){
            throw new PermissionException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }

        List<CartItemVO> cartItemVOList = cartService.getCartItemVOListByUserId(user.getId());
        double totalAmount = cartService.getCartTotalAmountByUserId(user.getId());
        model.addAttribute("cartItemVOList", cartItemVOList);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("refererURL", refererURL);
        return "cart";
    }
}
package com.netease.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netease.constant.ExceptionMsg;
import com.netease.constant.ResponseCode;
import com.netease.constant.ResponseMsg;
import com.netease.exception.ParamException;
import com.netease.exception.PermissionException;
import com.netease.model.CartItem;
import com.netease.model.User;
import com.netease.service.OrderService;
import com.netease.util.JsonUtils;
import com.netease.vo.OrderItemVO;
import com.netease.vo.Response;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @RequestMapping("/list.page")
    public String showOrderListPage(HttpServletRequest request, Model model) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null || user.getId() == null) {
            throw new PermissionException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }

        List<OrderItemVO> orderItemVOList = orderService.getOrderItemVOListByUserId(user.getId());
        Double totalAmount = orderService.getOrderTotalAmountByUserId(user.getId());
        model.addAttribute("orderItemVOList", orderItemVOList);
        model.addAttribute("totalAmount", totalAmount);
        return "order_list";
    }

    //下单
    @RequestMapping("/place")
    public String  placeOrder(CartItem cartItem, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null || user.getId() == null) {
            throw new PermissionException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }
        if (cartItem == null || cartItem.getId() == null) {
            throw new ParamException(ExceptionMsg.ILLEGAL_PARAMETER_EXCEPTION);
        }
        orderService.generateOrder(cartItem, user.getId());
        return "redirect:/order/list.page";
    }

    //批量下单
    @RequestMapping("/batch")
    @ResponseBody
    public String  batchOrder(Integer[] cartItemIds,Integer[] goodsIds, Integer[] quantities, HttpServletRequest request) throws JsonProcessingException {

        User user = (User) request.getSession().getAttribute("user");
        if (cartItemIds == null || cartItemIds.length <= 0 || goodsIds == null || goodsIds.length <= 0 || quantities == null || quantities.length <= 0) {
            throw new ParamException(ExceptionMsg.ILLEGAL_PARAMETER_EXCEPTION);
        }

        if (user == null || user.getId() == null) {
            throw new PermissionException(ExceptionMsg.ILLEGAL_ACCESS_EXCEPTION);
        }
        Response response = new Response();
        try {
            orderService.batchGenerateOrder(cartItemIds, goodsIds, quantities, user.getId());
            response.setCode(ResponseCode.ORDER_SUCCESS_CODE);
            response.setMessage(ResponseMsg.ORDER_SUCCESS_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            response.setCode(ResponseCode.ORDER_FAILED_CODE);
            response.setMessage(ResponseMsg.ORDER_FAILED_MESSAGE);
        }
        return JsonUtils.obj2String(response);
    }
}

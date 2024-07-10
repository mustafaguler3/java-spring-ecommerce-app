package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.Address;
import com.example.thymeleaf_demo.domain.Coupon;
import com.example.thymeleaf_demo.domain.Payment;
import com.example.thymeleaf_demo.dto.CartDto;
import com.example.thymeleaf_demo.dto.CartItemDto;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.repository.CouponRepository;
import com.example.thymeleaf_demo.service.CartService;
import com.example.thymeleaf_demo.service.OrderService;
import com.example.thymeleaf_demo.service.PaymentService;
import com.example.thymeleaf_demo.service.UserService;
import groovy.util.logging.Log4j2;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class CheckoutController {

    private static final Logger log = LoggerFactory.getLogger(CheckoutController.class);
    @Autowired
    private CartService cartService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private CouponRepository couponRepository;

    @GetMapping("/checkout")
    public String showCheckoutForm(@RequestParam(value = "couponCode",required = false) String couponCode,
                                   Model model,
                                   Principal principal) {
        UserDto userDto = userService.findByUsername(principal.getName());

        CartDto cartDto = cartService.findByUserId(userDto.getId());
        List<CartItemDto> cartItems = cartDto.getCartItems();
        model.addAttribute("couponCode", couponCode);
        //coupon code -> mshop2024

        Coupon appliedCoupon = null;

        if (couponCode != null) {
            appliedCoupon = couponRepository.findByCode(couponCode);
            //appliedCoupon.setCode(couponCode);
            //appliedCoupon.setDiscountAmount(10.0); // 10% discount
            //appliedCoupon.setExpiryDate(LocalDateTime.of(2024,8,30,23,59,59));
            model.addAttribute("youHaveCode","You have coupon code, apply now");
        }
        final Coupon finalAppliedCoupon = appliedCoupon;

        double totalAmount = cartItems.stream().mapToDouble(CartItemDto::getTotalAmount).sum();

        double discountedAmount =
                        cartItems
                        .stream()
                        .mapToDouble(item -> item.getDiscountAmount(finalAppliedCoupon))
                        .sum();

        log.info("Applied coupon {} ", appliedCoupon);
        log.info("Coupon Code {}",couponCode);
        log.info("Total {}",totalAmount);
        log.info("Discount {}",discountedAmount);

        model.addAttribute("coupon",appliedCoupon);
        model.addAttribute("cartSize",cartDto.getCartItems().size());
        model.addAttribute("cart", cartDto.getCartItems());
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("lastDiscountAmount", discountedAmount);
        model.addAttribute("discountAmount", totalAmount - discountedAmount); // 700-600 -> 100! discount


        model.addAttribute("address", new Address());
        model.addAttribute("payment", new Payment());
        return "checkout-form";
    }

    @PostMapping("/checkout")
    public String processCheckout(@ModelAttribute Address billingAddress,
                                  @ModelAttribute Payment paymentInfo,
                                  Model model) {
        // Process billing and payment information
        // Save to database, call payment gateway, etc.

        // Redirect to order confirmation page
        return "redirect:/order-confirmation";
    }


}



























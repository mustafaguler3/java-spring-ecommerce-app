package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.*;
import com.example.thymeleaf_demo.dto.*;
import com.example.thymeleaf_demo.enums.PaymentMethodType;
import com.example.thymeleaf_demo.repository.CouponRepository;
import com.example.thymeleaf_demo.repository.ProductRepository;
import com.example.thymeleaf_demo.service.*;
import com.example.thymeleaf_demo.util.DTOConverter;
import com.example.thymeleaf_demo.util.LyzicoService;
import com.iyzipay.model.CheckoutFormInitialize;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private BasketService basketService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DTOConverter dtoConverter;
    @Autowired
    private LyzicoService lyzicoService;

    @GetMapping("/checkout")
    public String showCheckoutForm(@RequestParam(value = "couponCode",required = false) String couponCode,
                                   Model model,
                                   Principal principal) {
        UserDto userDto = userService.findByUsername(principal.getName());

        BasketDto basketDto = basketService.findByUserId(userDto.getId());
        List<BasketItemDto> cartItems = basketDto.getBasketItems();
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

        double totalAmount = cartItems.stream().mapToDouble(BasketItemDto::getTotalAmount).sum();

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
        model.addAttribute("cartSize", basketDto.getBasketItems().size());
        model.addAttribute("basket", basketDto.getBasketItems());
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("lastDiscountAmount", discountedAmount);
        model.addAttribute("discountAmount", totalAmount - discountedAmount); // 700-600 -> 100! discount

        log.info("payment Types {}",PaymentMethodType.values());

        model.addAttribute("address", new Address());
        model.addAttribute("payment", new Payment());
        return "checkout-form";
    }

    @PostMapping("/checkout")
    public String processCheckout(
            @ModelAttribute("address") Address billingAddress,
                                  @ModelAttribute("payment") Payment paymentInfo,
                                  @RequestParam(value = "couponCode",required = false) String couponCode,
                                  Model model,
                                  Principal principal) {
        UserDto userDto = userService.findByUsername(principal.getName());
        BasketDto basketDto = basketService.findByUserId(userDto.getId());
        List<BasketItemDto> cartItems = basketDto.getBasketItems();
        Coupon appliedCoupon = null;

        if (couponCode != null) {
            appliedCoupon = couponRepository.findByCode(couponCode);
            //appliedCoupon.setCode(couponCode);
            //appliedCoupon.setDiscountAmount(10.0); // 10% discount
            //appliedCoupon.setExpiryDate(LocalDateTime.of(2024,8,30,23,59,59));
            model.addAttribute("youHaveCode","You have coupon code, apply now");
        }
        final Coupon finalAppliedCoupon = appliedCoupon;

        double totalAmount = cartItems.stream().mapToDouble(BasketItemDto::getTotalAmount).sum();

        double discountedAmount =
                cartItems
                        .stream()
                        .mapToDouble(item -> item.getDiscountAmount(finalAppliedCoupon))
                        .sum();
        long amount = (long) (appliedCoupon != null ? discountedAmount : totalAmount);


        CheckoutFormInitialize checkoutFormInitialize =
                lyzicoService.createPayment(amount, String.valueOf(userDto.getId()));

        model.addAttribute("checkoutFormContent",
                checkoutFormInitialize);
        model.addAttribute("totalAmount", totalAmount);

        basketService.deleteBasketByBasketId(basketDto.getId());

        return "redirect:"+checkoutFormInitialize.getPaymentPageUrl();
    }

    @GetMapping("/order-confirmation")
    public String orderConfirmation(
                                    Model model){
        // Verify the payment result using the token
        return "order-confirmation";
    }

}



























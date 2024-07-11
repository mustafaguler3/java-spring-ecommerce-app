package com.example.thymeleaf_demo.controller;

import com.example.thymeleaf_demo.domain.*;
import com.example.thymeleaf_demo.dto.*;
import com.example.thymeleaf_demo.enums.PaymentMethodType;
import com.example.thymeleaf_demo.repository.CouponRepository;
import com.example.thymeleaf_demo.repository.ProductRepository;
import com.example.thymeleaf_demo.service.*;
import com.example.thymeleaf_demo.util.DTOConverter;
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
    private CartService cartService;
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

    @GetMapping("/checkout")
    public String showCheckoutForm(@RequestParam(value = "couponCode",required = false) String couponCode,
                                   Model model,
                                   Principal principal) {
        UserDto userDto = userService.findByUsername(principal.getName());

        BasketDto basketDto = cartService.findByUserId(userDto.getId());
        List<BasketItemDto> cartItems = basketDto.getCartItems();
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
        model.addAttribute("cartSize", basketDto.getCartItems().size());
        model.addAttribute("cart", basketDto.getCartItems());
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
        BasketDto basketDto = cartService.findByUserId(userDto.getId());
        List<BasketItemDto> cartItems = basketDto.getCartItems();
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



        // Process billing and payment information


        userDto.setAddress(billingAddress.getAddress());
        userDto.setAddress2(billingAddress.getAddress2());
        userDto.setCity(billingAddress.getCity());
        userDto.setState(billingAddress.getState());
        userDto.setZipCode(billingAddress.getZipCode());

        Payment payment = new Payment();
        payment.setPaymentStatus("Success");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setCardCVV(paymentInfo.getCardCVV());
        payment.setCardNumber(paymentInfo.getCardNumber());
        payment.setAmount(appliedCoupon != null ? discountedAmount : totalAmount);

        payment.setNameOnCard(paymentInfo.getNameOnCard());
        payment.setCardExpiryDate(paymentInfo.getCardExpiryDate());
        payment.setCurrency("$");
        payment.setPaymentMethod(paymentInfo.getPaymentMethod());



        Order order = new Order();

        paymentService.processPayment(payment);

        order.setOrderDate(LocalDateTime.now());
        order.setUser(dtoConverter.convertToEntity(userDto));
        order.setPayment(payment);
        order.setStatus("Success");

        // Convert CartItems to OrderItems
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            Product product = productRepository.findProductById(cartItem.getProductId());
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        orderService.saveOrder(order);
        payment.setOrder(order);


        model.addAttribute("paymentMethod", payment.getPaymentMethod().name());
        model.addAttribute("totalAmount",order.getTotalAmount());
        // Save to database, call payment gateway, etc.

        // Redirect to order confirmation page
        return "redirect:/order-confirmation";
    }

    @GetMapping("/order-confirmation")
    public String orderConfirmation(@RequestParam Order order,
                                    Model model){
        model.addAttribute("order",order);
        return "order-confirmation";
    }

    @GetMapping("/b")
    public String b(){
        return "b";
    }
}



























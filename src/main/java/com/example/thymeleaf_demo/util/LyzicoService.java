package com.example.thymeleaf_demo.util;

import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.service.BasketService;
import com.example.thymeleaf_demo.service.UserService;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreateCheckoutFormInitializeRequest;
import com.iyzipay.request.RetrieveCheckoutFormRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LyzicoService {

    @Value("${iyzico.apiKey}")
    private String apiKey;

    @Value("${iyzico.secretKey}")
    private String secretKey;

    @Value("${iyzico.baseUrl}")
    private String baseUrl;

    @Autowired
    private UserService userService;
    @Autowired
    private BasketService basketService;

    public boolean verifyPayment(String token) {
        Options options = new Options();
        options.setApiKey(apiKey);
        options.setSecretKey(secretKey);
        options.setBaseUrl(baseUrl);

        RetrieveCheckoutFormRequest request = new RetrieveCheckoutFormRequest();
        request.setToken(token);

        CheckoutForm checkoutForm = CheckoutForm.retrieve(request, options);

        return "SUCCESS".equals(checkoutForm.getPaymentStatus());
    }

    public CheckoutFormInitialize createPayment(long amount, String buyerId){
        Options options = new Options();
        options.setApiKey(apiKey);
        options.setSecretKey(secretKey);
        options.setBaseUrl(baseUrl);

        CreateCheckoutFormInitializeRequest request = new CreateCheckoutFormInitializeRequest();
        request.setCallbackUrl("https://localhost:8080/order-confirmation");
        request.setLocale("tr");
        request.setConversationId(buyerId);
        request.setPrice(BigDecimal.valueOf(amount));
        request.setPaidPrice(BigDecimal.valueOf(amount));
        request.setCurrency("TRY");
        request.setBasketId("B67832");
        request.setPaymentGroup("PRODUCT");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String curretUser = authentication.getName();
        UserDto userDto = userService.findByUsername(curretUser);

        Buyer buyer = new Buyer();
        buyer.setId(buyerId);
        buyer.setName(userDto.getFirstName());
        buyer.setSurname(userDto.getLastName());
        buyer.setGsmNumber(userDto.getPhone());
        buyer.setEmail(userDto.getEmail());
        buyer.setIdentityNumber("74300864791");
        buyer.setLastLoginDate("2015-10-05 12:43:35");
        buyer.setRegistrationDate(LocalDateTime.now().toString());
        buyer.setRegistrationAddress(userDto.getAddress());
        buyer.setIp("85.34.78.112");
        buyer.setCity(userDto.getCity());
        buyer.setCountry(userDto.getCountry());
        buyer.setZipCode(userDto.getZipCode());
        request.setBuyer(buyer);

        Address shippingAddress = new Address();
        shippingAddress.setContactName("Jane Doe");
        shippingAddress.setCity("Istanbul");
        shippingAddress.setCountry("Turkey");
        shippingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        shippingAddress.setZipCode("34742");
        request.setShippingAddress(shippingAddress);

        Address billingAddress = new Address();
        billingAddress.setContactName("Jane Doe");
        billingAddress.setCity("Istanbul");
        billingAddress.setCountry("Turkey");
        billingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        billingAddress.setZipCode("34742");
        request.setBillingAddress(billingAddress);

        List<BasketItem> basketItems = new ArrayList<>();
        BasketItem firstBasketItem = new BasketItem();
        firstBasketItem.setId("BI101");
        firstBasketItem.setName("Binocular");
        firstBasketItem.setCategory1("Collectibles");
        firstBasketItem.setCategory2("Accessories");
        firstBasketItem.setItemType(BasketItemType.PHYSICAL.name());
        firstBasketItem.setPrice(BigDecimal.valueOf(amount));
        basketItems.add(firstBasketItem);

        request.setBasketItems(basketItems);

        CheckoutFormInitialize checkoutFormInitialize =
                CheckoutFormInitialize.create(request, options);

        return checkoutFormInitialize;
    }

}

















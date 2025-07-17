package com.demo.demo.api;

import java.util.Date;

import com.demo.demo.entity.User.User;
import com.demo.demo.enums.Role;
import com.demo.demo.repository.User.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

@Controller
public class CheckoutController {

    private final PayOS payOS;
    private final UserRepository userRepository;
    public CheckoutController(PayOS payOS, UserRepository userRepository) {
        super();
        this.payOS = payOS;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/")
    public String Index() {
        return "index";
    }

    @RequestMapping(value = "/success")
    public String Success(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setRole(Role.MEMBER);
        userRepository.save(user);
        return "success";
    }

    @RequestMapping(value = "/cancel")
    public String Cancel() {
        return "cancel";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create-payment-link", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void checkout(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        try {
            final String baseUrl = getBaseUrl(request);
            final String productName = "Antismo Premium - 1 Thang";
            final String description = "hihihihi";
            final String returnUrl = baseUrl + "/dashboard?ps=sucessful";
            final String cancelUrl = baseUrl + "/dashboard?ps=failed";
            final int price = 200000;
            // Gen order code
            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
            ItemData item = ItemData.builder().name(productName).quantity(1).price(price).build();
            PaymentData paymentData = PaymentData.builder().orderCode(orderCode).amount(price).description(description)
                    .returnUrl(returnUrl).cancelUrl(cancelUrl).item(item).build();
            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            String checkoutUrl = data.getCheckoutUrl();

            httpServletResponse.setHeader("Location", checkoutUrl);
            httpServletResponse.setStatus(302);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        String url = scheme + "://" + serverName;
        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            url += ":" + serverPort;
        }
        url += contextPath;
        return url;
    }
    
}
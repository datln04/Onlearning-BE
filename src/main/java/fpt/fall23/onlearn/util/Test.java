package fpt.fall23.onlearn.util;


import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.orders.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {
//
//    public static void main(String[] args) throws IOException {
//        Order order = null;
//        // Construct a request object and set desired parameters
//        // Here, OrdersCreateRequest() creates a POST request to /v2/checkout/orders
//        OrderRequest orderRequest = new OrderRequest();
//        orderRequest.checkoutPaymentIntent("CAPTURE");
//        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
//        purchaseUnits
//                .add(new PurchaseUnitRequest().amountWithBreakdown(new AmountWithBreakdown().currencyCode("USD").value("100.00")));
//        orderRequest.purchaseUnits(purchaseUnits);
//
//        ApplicationContext applicationContext = new ApplicationContext();
//        applicationContext.cancelUrl("http://localhost:8080/cancel.html");
//        applicationContext.returnUrl("http://localhost:8080/api/v1/paypal/review");
//        orderRequest.applicationContext(applicationContext);
//
//        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);
//
//        try {
//            // Call API with your client and get a response for your call
//            HttpResponse<Order> response = Credentials.client.execute(request);
//
//            // If call returns body in response, you can get the de-serialized version by
//            // calling result() on the response
//            order = response.result();
//            System.out.println("Order ID: " + order.id());
//            order.links().forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));
//        } catch (IOException ioe) {
//            if (ioe instanceof HttpException) {
//                // Something went wrong server-side
//                HttpException he = (HttpException) ioe;
//                System.out.println(he.getMessage());
//                he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
//            } else {
//                // Something went wrong client-side
//            }
//        }
//    }

    public static void main(String[] args) {
        Order order = null;
        OrdersCaptureRequest request = new OrdersCaptureRequest("1P421468L0410142V");

        try {
            // Call API with your client and get a response for your call
            HttpResponse<Order> response = Credentials.client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();
            System.out.println("Capture ID: " + order.purchaseUnits().get(0).payments().captures().get(0).id());
            order.purchaseUnits().get(0).payments().captures().get(0).links()
                    .forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));
        } catch (IOException ioe) {
            if (ioe instanceof HttpException) {
                // Something went wrong server-side
                HttpException he = (HttpException) ioe;
                System.out.println(he.getMessage());
                he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
            } else {
                // Something went wrong client-side
            }
        }
    }


}

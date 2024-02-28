package fpt.fall23.onlearn.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.*;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpRequest;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import com.paypal.orders.Capture;
import com.paypal.orders.LinkDescription;
import com.paypal.orders.Order;

import com.paypal.payouts.*;
import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.paypal.request.CreateOrderRequest;
import fpt.fall23.onlearn.dto.paypal.request.PayoutRequest;
import fpt.fall23.onlearn.dto.paypal.response.LinkDescriptionResponse;
import fpt.fall23.onlearn.dto.paypal.response.OrderResponse;
import fpt.fall23.onlearn.dto.withdrawal.WithdrawRequestView;
import fpt.fall23.onlearn.entity.*;
import fpt.fall23.onlearn.entity.PaymentHistory;
import fpt.fall23.onlearn.entity.Transaction;
import fpt.fall23.onlearn.enums.PaymentHistoryStatus;
import fpt.fall23.onlearn.enums.PaymentHistoryType;
import fpt.fall23.onlearn.enums.Role;
import fpt.fall23.onlearn.service.*;
import fpt.fall23.onlearn.util.Credentials;
import fpt.fall23.onlearn.util.CurrencyConverter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.val;
import org.hibernate.validator.internal.constraintvalidators.bv.money.CurrencyValidatorForMonetaryAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController
@RequestMapping("/api/v1/paypal")
public class PaypalV2Controller {


    @Value("${server.path}")
    private String serverPath;


    @Value("${server.paypalCaptureUrl}")
    private String paypalCaptureUrl;


    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PaymentHistoryService paymentHistoryService;

    @Autowired
    WalletService walletService;

    @Autowired
    StudentService studentService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/capture")
    public ResponseEntity<OrderResponse> getCaptureOrder(@RequestParam(name = "token") String token,
                                                         @RequestParam(name = "PayerID") String payerId) {
        Order order = null;
        OrdersCaptureRequest request = new OrdersCaptureRequest(token);
        // Call API with your client and get a response for your call
        PayPalEnvironment environment = new PayPalEnvironment.Sandbox(Credentials.clientId, Credentials.secret);
        PayPalHttpClient client = new PayPalHttpClient(environment);

        try {
            HttpResponse<Order> response = client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();
            System.out.println("Capture ID: " + order.purchaseUnits().get(0).payments().captures().get(0).id());
            order.purchaseUnits().get(0).payments().captures().get(0).links()
                    .forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(order.id());
            orderResponse.setStatus(order.status());
            Capture capture = order.purchaseUnits().get(0).payments()
                    .captures().get(0);
            List<LinkDescriptionResponse> linkDescriptionResponses = new ArrayList<>();
            for (LinkDescription des :
                    capture.links()) {
                LinkDescriptionResponse linkDescriptionResponse = new LinkDescriptionResponse();
                linkDescriptionResponse.setHref(des.href());
                linkDescriptionResponse.setRel(des.rel());
                linkDescriptionResponse.setMethod(des.method());
                linkDescriptionResponses.add(linkDescriptionResponse);
            }
            orderResponse.setLinks(linkDescriptionResponses);

            if (order.status().equalsIgnoreCase("COMPLETED")) {
                PaymentHistory paymentHistory = paymentHistoryService.getByPaymentOrderId(order.id());
                paymentHistory.setPaymentHistoryStatus(PaymentHistoryStatus.COMPLETED);
                paymentHistory.setCardDetails(order.payer().email());
                paymentHistory.setBankInformation(order.payer().payerId());
                paymentHistory.setOrderId(order.id());
                paymentHistory.setCaptureId(capture.id());
                paymentHistory.setTransactionDate(LocalDateTime.now());
                paymentHistory.setPaymentHistoryType(PaymentHistoryType.DEPOSIT);

                Account account = authenticationService.findAccountById(paymentHistory.getAccount().getId());
                Wallet wallet = walletService.getWalletByAccountId(account.getId());
                if (wallet == null) {
                    wallet = new Wallet();
                    wallet.setAmount(0.0);
                    wallet.setAccount(account);
                    wallet = walletService.saveWallet(wallet);
                }
                wallet.setBankName("PAYPAL");
                wallet.setBankNumber(order.payer().email());
                wallet.setAmount(paymentHistory.getAmount() + (wallet.getAmount() == null ? 0 : wallet.getAmount()));
                wallet = walletService.saveWallet(wallet);
                paymentHistory.setWallet(wallet);
                paymentHistory = paymentHistoryService.savePaymentHistory(paymentHistory);
            }
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        } catch (Exception e) {
            PaymentHistory paymentHistory = paymentHistoryService.getByPaymentOrderId(order.id());
            paymentHistory.setPaymentHistoryStatus(PaymentHistoryStatus.PENDING);
            paymentHistoryService.savePaymentHistory(paymentHistory);
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @Autowired
    EnrollService enrollService;

    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @PostMapping("/orders")
    @CrossOrigin
    public ResponseDTO<OrderResponse> createOrders(@RequestBody CreateOrderRequest createOrderRequest) {
        Order order = null;

        if (enrollService.checkIsBannedAction(createOrderRequest.getAccountId())) {
//            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Bạn đã bị hạn chế hành động do quá số lần hoàn tiền", null);
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(),"Bạn đã bị hạn chế hành động do quá số lần hoàn tiền", null);
        }

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        Double usdAmount = CurrencyConverter.convertVNDtoUSD(Double.valueOf(createOrderRequest.getValue()));
        Double vndAmount = Double.valueOf(createOrderRequest.getValue());
//        if(vndAmount < 10 || usdAmount > 1000){
//            LOGGER.info("Cannot create order out of range 10 and 1000");
//            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Cannot create order out of range 10 and 1000", null);
//        }

        purchaseUnits
                .add(new PurchaseUnitRequest().amountWithBreakdown(new AmountWithBreakdown().currencyCode("USD").value(String.valueOf(usdAmount))));
        orderRequest.purchaseUnits(purchaseUnits);
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.cancelUrl(serverPath + "/api/v1/paypal/cancel");
        applicationContext.returnUrl(paypalCaptureUrl);
        orderRequest.applicationContext(applicationContext);
        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        try {
            PayPalEnvironment environment = new PayPalEnvironment.Sandbox(Credentials.clientId, Credentials.secret);
            PayPalHttpClient client = new PayPalHttpClient(environment);

            // Call API with your client and get a response for your call
            HttpResponse<Order> response = client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();

            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(order.id());
            orderResponse.setStatus(order.status());
            List<LinkDescription> linkDescriptions = order.links();
            List<LinkDescriptionResponse> linkDescriptionResponses = new ArrayList<>();
            for (LinkDescription des :
                    linkDescriptions) {
                LinkDescriptionResponse linkDescriptionResponse = new LinkDescriptionResponse();
                linkDescriptionResponse.setHref(des.href());
                linkDescriptionResponse.setRel(des.rel());
                linkDescriptionResponse.setMethod(des.method());
                linkDescriptionResponses.add(linkDescriptionResponse);
            }
            orderResponse.setLinks(linkDescriptionResponses);

            //save payment_history status = create
            PaymentHistory paymentHistory = new PaymentHistory();
            paymentHistory.setPaymentMethod("paypal");
            paymentHistory.setTransactionDate(LocalDateTime.now());
            paymentHistory.setAmount(Double.valueOf(createOrderRequest.getValue()));
            paymentHistory.setPaymentHistoryStatus(PaymentHistoryStatus.CREATED);
            paymentHistory.setOrderId(order.id());
            Account account = authenticationService.findAccountById(createOrderRequest.getAccountId());
            Wallet wallet = walletService.getWalletByAccountId(account.getId());
            if (wallet == null) {
                wallet = new Wallet();
                wallet.setAmount(0.0);
                wallet.setAccount(account);
                wallet = walletService.saveWallet(wallet);
            }
            paymentHistory.setWallet(wallet);
            Student student = studentService.findStudentByAccountId(account.getId());
            if (student != null) {
                paymentHistory.setStudent(student);
            }
            Teacher teacher = teacherService.getTeacherByAccountId(account.getId());
            if (teacher != null) {
                paymentHistory.setTeacher(teacher);
            }
            if (account != null) {
                paymentHistory.setAccount(account);
            }
            paymentHistoryService.savePaymentHistory(paymentHistory);
            return new ResponseDTO<>(HttpStatus.OK.value(), "Create order success", orderResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "fail to create paypal order", null);
    }


    @GetMapping("/confirm")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Object> confirmOrder(@RequestParam(name = "token") String token) {
        Order order = null;
//        OrdersCaptureRequest request = new (token);
        OrdersGetRequest request = new OrdersGetRequest(token);

        try {
            PayPalEnvironment environment = new PayPalEnvironment.Sandbox(Credentials.clientId, Credentials.secret);
            PayPalHttpClient client = new PayPalHttpClient(environment);

            // Call API with your client and get a response for your call
            HttpResponse<Order> response = client.execute(request);
            order = response.result();

            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(order.id());
            orderResponse.setStatus(order.status());
            List<LinkDescription> linkDescriptions = order.links();
            List<LinkDescriptionResponse> linkDescriptionResponses = new ArrayList<>();
            for (LinkDescription des :
                    linkDescriptions) {
                LinkDescriptionResponse linkDescriptionResponse = new LinkDescriptionResponse();
                linkDescriptionResponse.setHref(des.href());
                linkDescriptionResponse.setRel(des.rel());
                linkDescriptionResponse.setMethod(des.method());
                linkDescriptionResponses.add(linkDescriptionResponse);
            }
            orderResponse.setLinks(linkDescriptionResponses);

            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("400", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/cancel")
    public ResponseEntity<Object> cancelOrder(@RequestParam(name = "token") String token) {
        PaymentHistory paymentHistory = paymentHistoryService.getByPaymentOrderId(token);
        if (paymentHistory == null) {
            return new ResponseEntity<>("400", HttpStatus.BAD_REQUEST);
        }
        paymentHistory.setPaymentHistoryStatus(PaymentHistoryStatus.CANCEL);
        return new ResponseEntity<>(paymentHistoryService.savePaymentHistory(paymentHistory), HttpStatus.OK);
    }


    @Autowired
    WithdrawRequestService withdrawRequestService;

    @Autowired
    TransactionService transactionService;

    @PostMapping("/payout")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView({WithdrawRequestView.class})
    public ResponseDTO<PaymentHistory> payoutWithdraw(@RequestBody PayoutRequest payoutRequest) {

        Account account = authenticationService.findAccountById(payoutRequest.getAccountId());
        if (account == null) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Can't find account", null);
        }
        Wallet wallet = walletService.getWalletByAccountId(account.getId());

        if (wallet == null) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Can't find wallet", null);
        }

        if (wallet.getAmount() < Double.valueOf(payoutRequest.getAmountValue())) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Wallet amount doesn't enough", null);
        }

        CreatePayoutRequest request = new CreatePayoutRequest();

        Double usdAmount = CurrencyConverter.convertVNDtoUSD(Double.valueOf(payoutRequest.getAmountValue()));
//        if(usdAmount < 10 || usdAmount > 1000){
//            LOGGER.info("Cannot withdraw out of range 10 and 1000");
//            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Cannot withdraw out of range 10 and 1000", null);
//        }

        List<com.paypal.payouts.PayoutItem> payoutItems = getPayoutItems(account, String.valueOf(usdAmount), wallet);
        request.items(payoutItems);

        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setAmount(Double.valueOf(payoutRequest.getAmountValue()));
        paymentHistory.setPaymentHistoryStatus(PaymentHistoryStatus.PENDING_PAYOUT);
        paymentHistory.setPaymentHistoryType(PaymentHistoryType.WITHDRAW);
        paymentHistory.setPaymentMethod("PAYPAL");
        paymentHistory.setTransactionDate(LocalDateTime.now());
        paymentHistory.setAccount(account);
        if(account.getRole().equals(Role.TEACHER)){
            Teacher teacher = teacherService.getTeacherByAccountId(account.getId());
            paymentHistory.setTeacher(teacher);
        }
        if(account.getRole().equals(Role.STUDENT)){
            Student student = studentService.findStudentByAccountId(account.getId());
            paymentHistory.setStudent(student);
        }


        paymentHistory = paymentHistoryService.savePaymentHistory(paymentHistory);


        com.paypal.payouts.SenderBatchHeader senderBatchHeader = new com.paypal.payouts.SenderBatchHeader();
        senderBatchHeader.emailSubject("You have an payout !");
        request.senderBatchHeader(senderBatchHeader);
        try {
            PayPalEnvironment environment = new PayPalEnvironment.Sandbox(Credentials.clientId, Credentials.secret);
            PayPalHttpClient client = new PayPalHttpClient(environment);

            PayoutsPostRequest httpRequest = new PayoutsPostRequest().requestBody(request);
            HttpResponse<CreatePayoutResponse> response = client.execute(httpRequest);

            CreatePayoutResponse payouts = response.result();
            System.out.println("Payouts Batch ID: " + payouts.batchHeader().payoutBatchId());
            payouts.links().forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));

            paymentHistory.setBankInformation(payouts.batchHeader().senderBatchHeader().senderBatchId());
            paymentHistory.setPayoutBatchId(payouts.batchHeader().payoutBatchId());
            paymentHistoryService.savePaymentHistory(paymentHistory);

            Thread.sleep(2000);

            PayoutsGetRequest batchRequest = new PayoutsGetRequest(payouts.batchHeader().payoutBatchId())
                    .page(1)
                    .pageSize(10)
                    .totalRequired(true);
            HttpResponse<com.paypal.payouts.PayoutBatch> batchResponse = client.execute(batchRequest);
            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            com.paypal.payouts.PayoutBatch batchPayout = batchResponse.result();
            System.out.println("Payouts Batch ID: " + batchPayout.batchHeader().payoutBatchId());
            System.out.println("Payouts Batch Status: " + batchPayout.batchHeader().batchStatus());
            batchPayout.links()
                    .forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));

            if (batchPayout.batchHeader().batchStatus().equalsIgnoreCase("SUCCESS")) {
                PayoutBatchItem payoutBatchItem = batchPayout.items().get(0);
                paymentHistory.setPaymentHistoryStatus(PaymentHistoryStatus.COMPLETED_PAYOUT);
                paymentHistory.setCardDetails(payoutBatchItem.payoutItem().receiver());
                paymentHistory.setBankInformation(wallet.getBankNumber());
                paymentHistory.setPayoutBatchItemId(payoutBatchItem.payoutItemId());
                wallet.setAmount(wallet.getAmount() - Double.valueOf(payoutRequest.getAmountValue()));
                wallet = walletService.saveWallet(wallet);
                paymentHistory.setWallet(wallet);
                paymentHistory = paymentHistoryService.savePaymentHistory(paymentHistory);
                return new ResponseDTO<>(HttpStatus.OK.value(), "success", paymentHistory);
            } else {
                System.out.println("Status is not success !");
                return new ResponseDTO<>(HttpStatus.OK.value(), "Status not success", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Payout fail", null);
    }

    private static List<com.paypal.payouts.PayoutItem> getPayoutItems(Account account, String amountValue, Wallet wallet) {
        List<com.paypal.payouts.PayoutItem> payoutItems = new ArrayList<>();
        com.paypal.payouts.PayoutItem payoutItem = new com.paypal.payouts.PayoutItem();
        payoutItem.receiver(wallet.getBankNumber());

        com.paypal.payouts.Currency amount = new com.paypal.payouts.Currency();
        amount.currency("USD");
        amount.value(amountValue);

        payoutItem.amount(amount);

        payoutItem.recipientType("EMAIL");
        payoutItem.note("Thanks for your serve !");

        payoutItems.add(payoutItem);
        return payoutItems;
    }


}

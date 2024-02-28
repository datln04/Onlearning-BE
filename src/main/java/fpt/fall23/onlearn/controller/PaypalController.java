//package fpt.fall23.onlearn.controller;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.logging.Logger;
//import javax.naming.NamingException;
//
//import com.paypal.api.payments.*;
//import fpt.fall23.onlearn.dto.paypal.response.PaypalPaymentResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.paypal.base.rest.APIContext;
//import com.paypal.base.rest.PayPalRESTException;
//import fpt.fall23.onlearn.config.OpenApiConfig;
//import fpt.fall23.onlearn.dto.paypal.request.CreateOrderRequest;
//import fpt.fall23.onlearn.dto.paypal.response.PaypalReview;
//import fpt.fall23.onlearn.entity.Account;
//import fpt.fall23.onlearn.entity.Student;
//import fpt.fall23.onlearn.entity.Teacher;
//import fpt.fall23.onlearn.entity.Wallet;
//import fpt.fall23.onlearn.service.AuthenticationService;
//import fpt.fall23.onlearn.service.PaymentHistoryService;
//import fpt.fall23.onlearn.service.StudentService;
//import fpt.fall23.onlearn.service.TeacherService;
//import fpt.fall23.onlearn.service.WalletService;
//import fpt.fall23.onlearn.util.Constants;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//
//@RestController
//@RequestMapping("/api/v1/paypal")
//public class PaypalController {
//
//    @Value("${server.path}")
//    private String serverPath;
//
//    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Autowired
//    PaymentHistoryService paymentHistoryService;
//
//    @Autowired
//    WalletService walletService;
//
//    @Autowired
//    StudentService studentService;
//
//    @Autowired
//    TeacherService teacherService;
//
//    @Autowired
//    AuthenticationService authenticationService;
//
//    @GetMapping("/review")
//    public Object getReviewPayment(@RequestParam(name = "paymentId") String paymentId,
//                                   @RequestParam(name = "PayerID") String payerId) {
////        Payment payment;
////        try {
////            payment = getPaymentDetails(paymentId);
////            PayerInfo payerInfo = payment.getPayer().getPayerInfo();
////            Transaction transaction = payment.getTransactions().get(0);
////            PaypalReview paypalReview = new PaypalReview(payerInfo, transaction);
////
////            if (payerInfo != null && transaction != null) {
////                Account account = authenticationService
////                        .findAccountById(Long.valueOf(transaction.getItemList().getItems().get(0).getSku()));
////                fpt.fall23.onlearn.entity.PaymentHistory paymentHistory = new fpt.fall23.onlearn.entity.PaymentHistory();
////                paymentHistory.setPaymentMethod("paypal");
////                paymentHistory.setTransactionDate(new Date());
////                paymentHistory.setAmount(Double.valueOf(transaction.getAmount().getTotal()));
////                paymentHistory.setStatus("SUCCESS");
////                paymentHistory.setCardDetails(payerInfo.getPayerId());
////                paymentHistory.setBankInformation(payerInfo.getPayerId());
////                paymentHistory.setPaymentId(paymentId);
////                Wallet wallet = walletService.getWalletByAccountId(account.getId());
////                if (wallet == null) {
////                    wallet = new Wallet();
////                    wallet.setAmount(0.0);
////                    wallet.setAccount(account);
////                    wallet = walletService.saveWallet(wallet);
////                }
////                wallet.setBankName("PAYPAL");
////                wallet.setBankNumber(payerInfo.getPayerId());
////                wallet.setAmount(Double.valueOf(transaction.getAmount().getTotal()) + (wallet.getAmount() == null ? 0 : wallet.getAmount()));
////                wallet = walletService.saveWallet(wallet);
////                paymentHistory.setWallet(wallet);
////
////                Student student = studentService.findStudentByAccountId(account.getId());
////                if (student != null) {
////                    paymentHistory.setStudent(student);
////                }
////
////                Teacher teacher = teacherService.getTeacherByAccountId(account.getId());
////                if (teacher != null) {
////                    paymentHistory.setTeacher(teacher);
////                }
////
////                if (account != null) {
////                    paymentHistory.setAccount(account);
////                }
////                paymentHistoryService.savePaymentHistory(paymentHistory);
////            }
////
////            return new ResponseEntity<>(paypalReview, HttpStatus.OK);
////        } catch (PayPalRESTException e) {
////            e.printStackTrace();
////        }
//        return new ResponseEntity<>(payerId, HttpStatus.BAD_REQUEST);
//    }
//
//    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
//    @PostMapping("/orders")
//    @CrossOrigin
//    public Object createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
//        Account account = authenticationService.findAccountById(createOrderRequest.getAccountId());
//        try {
//            Transaction listTransaction = getTransactionInformation(account, createOrderRequest.getValue());
//
//            APIContext apiContext = new APIContext(Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.CLIENT_MODE);
//
//            Order order = new Order();
//
//            Payment payment = new Payment();
//            payment.setIntent("sale");
//
//            Payer payer = getPayerInformation(account);
//            payment.setPayer(payer);
//
//            RedirectUrls redirectUrls = getRedirectURLs();
//            payment.setRedirectUrls(redirectUrls);
//            payment.setTransactions(Collections.singletonList(listTransaction)); // Set a single transaction
//
//            Payment approvedPayment = payment.create(apiContext);
//
//            String link = getApprovalLink(approvedPayment);
////			approvedPayment.
//            approvedPayment.getLinks();
//            PaypalPaymentResponse paypalPaymentResponse = new PaypalPaymentResponse();
//            paypalPaymentResponse.setId(approvedPayment.getId());
//            paypalPaymentResponse.setState(approvedPayment.getState());
//            paypalPaymentResponse.setLinks(approvedPayment.getLinks());
//            return new ResponseEntity<>(paypalPaymentResponse, HttpStatus.OK);
//
//        } catch (SQLException | NamingException | PayPalRESTException e) {
//            e.printStackTrace();
//        }
//        return new ResponseEntity<>("400", HttpStatus.BAD_REQUEST);
//
//    }
//
//    @SuppressWarnings("unused")
//    private Transaction getTransactionInformation(Account account, String depositValue)
//            throws SQLException, NamingException {
//        List<Item> items = new ArrayList<>();
//        ItemList itemList = new ItemList();
//        Transaction transaction = new Transaction();
//        Amount amount = new Amount();
//
//        transaction.setAmount(amount);
//        transaction.setDescription(String.format("Deposit for account %s", account.getProfile().getEmail()));
//
//        Item item = new Item();
//        item.setCurrency("USD");
//        item.setName("Nap tien tai khoan");
//
//        item.setPrice(depositValue);
//
//        item.setSku(String.format("%s", account.getId()));
//        item.setQuantity("1");
//        items.add(item);
//
//        itemList.setItems(items);
//        transaction.setItemList(itemList);
//
//        double totalAmount = items.stream().mapToDouble(i -> Double.parseDouble(i.getPrice())).sum();
//        amount.setCurrency("USD");
//        amount.setTotal(String.valueOf(totalAmount));
//
//        transaction.setAmount(amount);
//
//        return transaction;
//    }
//
//    private Payer getPayerInformation(Account p) {
//        Payer payer = new Payer();
//        payer.setPaymentMethod("paypal");
//        PayerInfo payerInfo = new PayerInfo();
//        payerInfo.setFirstName(p.getProfile().getFirstName()).setLastName(p.getProfile().getLastName())
//                .setEmail(p.getProfile().getEmail());
//        payer.setPayerInfo(payerInfo);
//
//        return payer;
//    }
//
//    private RedirectUrls getRedirectURLs() {
//        RedirectUrls redirectUrls = new RedirectUrls();
//        redirectUrls.setCancelUrl(serverPath + "/cancel.html");
//        redirectUrls.setReturnUrl(serverPath + "/api/v1/paypal/review");
//
//        return redirectUrls;
//    }
//
//    private String getApprovalLink(Payment approvedPayment) {
//        List<Links> links = approvedPayment.getLinks();
//        String approvalLink = null;
//        for (Links link : links) {
//            if (link.getRel().equalsIgnoreCase("approval_url")) {
//                approvalLink = link.getHref();
//                break;
//            }
//        }
//
//        return approvalLink;
//    }
//
//    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
//        APIContext apiContext = new APIContext(Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.CLIENT_MODE);
//        return Payment.get(apiContext, paymentId);
//    }
//}

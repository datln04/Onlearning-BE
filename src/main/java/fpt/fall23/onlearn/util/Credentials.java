package fpt.fall23.onlearn.util;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;

public class Credentials {

    public static String clientId = "AYrQHcn3xVy1_MNFrqGOvlpl-w182slLCBfSbFLjDYITvAw3a0GdIHMW4Fe4GAf05Kozz44wBet_kNoF";
    public static String secret = "EHwW8z7y6S90uvQZ0mnypkmk1kY4at2UjfCPz30bTC2pCy9C65__uBI4RvFBEApzl8HoMdjZ2KBYL6F9";

    // Creating a sandbox environment
    private static PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, secret);

    // Creating a client for the environment
    static PayPalHttpClient client = new PayPalHttpClient(environment);
}

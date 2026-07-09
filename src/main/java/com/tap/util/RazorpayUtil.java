package com.tap.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Minimal Razorpay integration using only the built-in JDK HTTP client and
 * javax.crypto — deliberately avoids pulling in the full razorpay-java SDK
 * (and its okhttp/org.json dependencies) to keep the project's "no extra
 * frameworks, just the listed jars" constraint intact. No additional JARs
 * are required in WEB-INF/lib for this class to work.
 *
 * SETUP: Create a free Razorpay account at https://dashboard.razorpay.com/signup,
 * switch to "Test Mode" (top-left toggle), go to Settings -> API Keys ->
 * Generate Test Key, and paste the Key Id / Key Secret below. Test mode lets
 * you complete full checkout flows with Razorpay's documented test card /
 * UPI details without moving any real money:
 * https://razorpay.com/docs/payments/payments/test-card-upi-details/
 */
public class RazorpayUtil {

    // ---- Update these with your own Razorpay TEST MODE keys ----
	private static final String KEY_ID     = "rzp_test_TAyaHHEQHxqElW";
	private static final String KEY_SECRET = "Hm5FaMvkrwR6ejQLWnBDtZpe";
    // ---------------------------------------------------------------

    private static final String ORDERS_URL = "https://api.razorpay.com/v1/orders";
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private RazorpayUtil() {}

    public static String getKeyId() {
        return KEY_ID;
    }

    /** Simple holder for the data the checkout page's JS needs. */
    public static class RazorpayOrder {
        public final String orderId;
        public final long amountInPaise;

        public RazorpayOrder(String orderId, long amountInPaise) {
            this.orderId = orderId;
            this.amountInPaise = amountInPaise;
        }
    }

    /**
     * Creates a Razorpay Order via their REST API (server-to-server call,
     * required before opening the Checkout.js modal on the client).
     * amountInRupees is converted to paise (Razorpay's smallest currency unit).
     * Returns null on any failure (network error, bad credentials, etc.).
     */
    public static RazorpayOrder createOrder(double amountInRupees, String receipt) {
        try {
            long amountInPaise = Math.round(amountInRupees * 100);
            String json = "{"
                    + "\"amount\":" + amountInPaise + ","
                    + "\"currency\":\"INR\","
                    + "\"receipt\":\"" + escapeJson(receipt) + "\""
                    + "}";

            String basicAuth = Base64.getEncoder().encodeToString(
                    (KEY_ID + ":" + KEY_SECRET).getBytes(StandardCharsets.UTF_8));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ORDERS_URL))
                    .timeout(Duration.ofSeconds(10))
                    .header("Authorization", "Basic " + basicAuth)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("RazorpayUtil: order creation failed, HTTP " + response.statusCode() + " - " + response.body());
                return null;
            }

            String orderId = extractJsonString(response.body(), "id");
            if (orderId == null) {
                System.err.println("RazorpayUtil: could not parse order id from response - " + response.body());
                return null;
            }
            return new RazorpayOrder(orderId, amountInPaise);

        } catch (Exception e) {
            System.err.println("RazorpayUtil: order creation error - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Verifies the payment signature Razorpay's Checkout.js hands back after
     * a successful payment. This is the standard Razorpay verification
     * algorithm: HMAC-SHA256 of "{order_id}|{payment_id}" using the account's
     * key secret, compared against the signature the client submitted.
     * NEVER trust a payment as successful without this check passing.
     */
    public static boolean verifySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        if (razorpayOrderId == null || razorpayPaymentId == null || razorpaySignature == null) {
            return false;
        }
        try {
            String payload = razorpayOrderId + "|" + razorpayPaymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(KEY_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hash) hex.append(String.format("%02x", b));

            return MessageDigest.isEqual(
                    hex.toString().getBytes(StandardCharsets.UTF_8),
                    razorpaySignature.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println("RazorpayUtil: signature verification error - " + e.getMessage());
            return false;
        }
    }

    private static String extractJsonString(String json, String key) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher m = p.matcher(json);
        return m.find() ? m.group(1) : null;
    }

    private static String escapeJson(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}


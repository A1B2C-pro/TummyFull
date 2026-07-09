# TummyFull — Full Stack Food Delivery Web App

Java Servlets (Jakarta EE 10 / Tomcat 10.1) + JSP + JDBC + MySQL + DAO pattern.
No Spring, no Hibernate, no Maven — plain Eclipse Dynamic Web Project.

## 1. Project Structure

```
TummyFull/
├── sql/
│   └── tapfood_schema.sql        <- run this first (schema + seed data)
├── src/
│   └── com/tap/
│       ├── model/                <- 15 model classes
│       ├── dao/                  <- DAO interfaces
│       ├── daoimpl/               <- JDBC DAO implementations
│       ├── util/                  <- BCrypt, Mail (OTP), DB connection, validation, session keys
│       ├── filter/                <- Customer / Admin / Agent auth filters
│       ├── listener/              <- App startup listener
│       └── servlet/                <- 30 servlets (mapped only via web.xml, no @WebServlet)
└── WebContent/
    ├── WEB-INF/
    │   ├── web.xml                <- all servlet/filter/listener mappings
    │   ├── lib/                    <- PUT REQUIRED JARS HERE (see section 3)
    │   └── jsp/{user,admin,delivery}/
    ├── css/  js/  images/
    ├── index.jsp, login.jsp, register.jsp, login-role.jsp,
    │   admin-login.jsp, agent-login.jsp, agent-register.jsp,
    │   otp-verify.jsp, forgot-password.jsp, reset-password.jsp,
    │   error-404.jsp, error-500.jsp
    └── includes/ (navbar, sidebar, footer, alerts)
```

## 2. Import into Eclipse

1. `File → Import → Dynamic Web Project` (or `General → Existing Projects into Workspace` after creating an empty Dynamic Web Project named **TummyFull** first, Target Runtime = **Apache Tomcat v10.1**, Dynamic Web Module version = **6.0**).
2. Copy `src/` contents into the project's `Java Resources/src`.
3. Copy `WebContent/` contents into the project's `WebContent` (or `src/main/webapp` if your Eclipse uses that layout — either works, just keep `WEB-INF` at the web root).
4. Right-click project → Properties → Java Build Path → confirm `src` is a source folder.

## 3. Required Libraries (place in `WebContent/WEB-INF/lib/`)

No Maven is used, so download these JARs manually and drop them into `WEB-INF/lib`. Direct Maven Central links:

| Library | Purpose | Download |
|---|---|---|
| mysql-connector-j 9.1.0 | JDBC driver | https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.1.0/mysql-connector-j-9.1.0.jar |
| jbcrypt 0.4 | Password hashing | https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar |
| jakarta.mail-api 2.1.3 | Mail API | https://repo1.maven.org/maven2/jakarta/mail/jakarta.mail-api/2.1.3/jakarta.mail-api-2.1.3.jar |
| angus-mail 2.0.3 | Mail implementation (sends the OTP email) | https://repo1.maven.org/maven2/org/eclipse/angus/angus-mail/2.0.3/angus-mail-2.0.3.jar |
| jakarta.activation-api 2.1.3 | Required by jakarta.mail | https://repo1.maven.org/maven2/jakarta/activation/jakarta.activation-api/2.1.3/jakarta.activation-api-2.1.3.jar |
| angus-activation 2.0.2 | Required by angus-mail | https://repo1.maven.org/maven2/org/eclipse/angus/angus-activation/2.0.2/angus-activation-2.0.2.jar |
| jakarta.servlet.jsp.jstl-api 3.0.0 | JSTL API (`<c:>`, `<fn:>` tags) | https://repo1.maven.org/maven2/jakarta/servlet/jsp/jstl/jakarta.servlet.jsp.jstl-api/3.0.0/jakarta.servlet.jsp.jstl-api-3.0.0.jar |
| jakarta.servlet.jsp.jstl 3.0.1 | JSTL implementation | https://repo1.maven.org/maven2/org/glassfish/web/jakarta.servlet.jsp.jstl/3.0.1/jakarta.servlet.jsp.jstl-3.0.1.jar |

Jakarta Servlet API and Jakarta Mail's underlying transport are already provided by Tomcat 10.1 itself — **do not** add a servlet-api jar or it will conflict with Tomcat's own.

After adding the jars, right-click each in Eclipse → **Build Path → Add to Build Path** (or just Deployment Assembly should pick up everything in `WEB-INF/lib` automatically).

## 4. Database Setup

1. Open MySQL Workbench or the `mysql` CLI.
2. Run the entire script in `sql/tapfood_schema.sql`. This creates the `tapfood` database, all 15 tables with foreign keys/indexes/a trigger, and seed data:
   - 1 admin, 3 delivery agents, 20 users, 5 restaurants, 30 menu items, offers, coupons, sample orders & reviews.
3. **Update DB credentials** in `src/com/tap/util/DBConnectionUtil.java` (`DB_URL`, `DB_USER`, `DB_PASSWORD`) to match your local MySQL instance.

### Test Login Credentials (all seeded passwords use real bcrypt hashes)

| Role | Email | Password |
|---|---|---|
| Admin | admin@tummyfull.com | Admin@123 |
| Delivery Agent | ravi.agent@tummyfull.com | Agent@123 |
| Customer | aarav1@example.com | User@123 |

(Any of the 20 seeded customers / 3 agents use the same password shown above.)

## 5. Gmail OTP Configuration

Edit `src/com/tap/util/MailUtil.java`:

```java
private static final String SENDER_EMAIL = "your-gmail-id@gmail.com";
private static final String SENDER_APP_PASSWORD = "your-16-char-app-password";
```

Gmail requires an **App Password** (not your normal password) once 2-Step Verification is enabled on the account. Generate one at:
https://myaccount.google.com/apppasswords

Without this configured, registration/login will still work but the OTP email send will silently fail (logged to console) — useful for local testing if you don't want to wire up real email yet. You can also read the generated OTP directly from the `otp_verification` table while testing.

## 6. Run

1. Add the project to a Tomcat 10.1 server in Eclipse (Servers view → New → Apache Tomcat v10.1).
2. Start the server.
3. Visit `http://localhost:8080/TummyFull/` — this loads `index.jsp`.

## 7. Application Flow Summary

- **Landing** → `index.jsp` → Login → Role select (Customer / Admin / Delivery Agent)
- **Customer**: Register → OTP email verification → Login → Browse restaurants (Bangalore only) → Menu → Cart (session-based, one restaurant at a time) → Checkout (coupon, GST, platform+delivery fee) → Place Order → Live tracking → Invoice download → Order history → Profile/address → Reviews
- **Admin**: Login → Dashboard (orders/revenue/users/restaurants/active deliveries/best seller/monthly sales/pending) → Manage Restaurants / Menu / Users / Orders (status + agent assignment) / Delivery Agents / Offers & Coupons / Categories / Reviews
- **Delivery Agent**: Register → OTP verification → Login → Dashboard → Availability toggle → Accept / Pick Up / Start Delivery / Mark Delivered (status changes reflect instantly for admin & customer since all read the same `orders` table)

## 8. Security Notes

- All passwords hashed with `BCrypt.hashpw()` / verified with `BCrypt.checkpw()`.
- Every query uses `PreparedStatement` — no string-concatenated SQL.
- `CustomerAuthFilter` / `AdminAuthFilter` / `AgentAuthFilter` block direct URL access to protected servlets when not logged in as the correct role.
- OTPs expire after 5 minutes (`OTPUtil.OTP_VALIDITY_MINUTES`) and are single-use (`is_used` flag); resend regenerates and invalidates the previous code.
- Logout invalidates the `HttpSession`.

## 9. Notes / Known Simplifications

- Invoice download is a plain-text file (no external PDF library was introduced to keep the "no extra frameworks" constraint tight); swap in a PDF lib later if you want a styled PDF.
- Payment methods (UPI/Credit/Debit Card) are captured and stored but there is no real payment gateway integration — this is a demo checkout flow, all non-COD orders are marked `PAID` immediately for demo purposes.
- used razorpay in Test Mode , where generate key id and secreat key, past in the code , use two step verification in google acount use generate key use in project
- Images are referenced via public Unsplash URLs (as requested, "for images add through links") — swap for your own hosted images/CDN for production use.

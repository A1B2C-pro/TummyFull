package com.tap.servlet;

import com.tap.dao.OrderDAO;
import com.tap.daoimpl.OrderDAOImpl;
import com.tap.model.Order;
import com.tap.model.OrderItem;
import com.tap.model.User;
import com.tap.util.SessionConstants;
import com.tap.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Generates a genuine downloadable PDF invoice for an order, using
 * Apache PDFBox (pdfbox-2.0.30.jar + fontbox-2.0.30.jar +
 * commons-logging-1.2.jar must be in WEB-INF/lib).
 */
public class InvoiceServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAOImpl();

    // Brand colors matching the site's dark-green / gold theme
    private static final PDColor BRAND_DARK  = new PDColor(new float[]{0.106f, 0.227f, 0.169f}, PDDeviceRGB.INSTANCE); // #1b3a2b
    private static final PDColor BRAND_GOLD  = new PDColor(new float[]{0.969f, 0.788f, 0.282f}, PDDeviceRGB.INSTANCE); // #f7c948
    private static final PDColor TEXT_DARK   = new PDColor(new float[]{0.12f, 0.12f, 0.12f}, PDDeviceRGB.INSTANCE);
    private static final PDColor TEXT_MUTED  = new PDColor(new float[]{0.45f, 0.45f, 0.45f}, PDDeviceRGB.INSTANCE);
    private static final PDColor LINE_COLOR  = new PDColor(new float[]{0.85f, 0.85f, 0.85f}, PDDeviceRGB.INSTANCE);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionConstants.CUSTOMER_USER);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int orderId = ValidationUtil.parseIntOrZero(request.getParameter("orderId"));
        Order order = orderDAO.getOrderById(orderId);

        if (order == null || order.getUserId() != user.getUserId()) {
            response.sendRedirect(request.getContextPath() + "/OrderHistoryServlet");
            return;
        }

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            PDFont bold    = PDType1Font.HELVETICA_BOLD;
            PDFont regular = PDType1Font.HELVETICA;

            float pageWidth = page.getMediaBox().getWidth();
            float margin = 50;
            float y = page.getMediaBox().getHeight() - 50;

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                // ---- Header banner ----
                cs.setNonStrokingColor(BRAND_DARK);
                cs.addRect(0, y - 20, pageWidth, 70);
                cs.fill();

                cs.beginText();
                cs.setNonStrokingColor(BRAND_GOLD);
                cs.setFont(bold, 22);
                cs.newLineAtOffset(margin, y + 15);
                cs.showText("TummyFull");
                cs.endText();

                cs.beginText();
                cs.setNonStrokingColor(1f, 1f, 1f);
                cs.setFont(regular, 10);
                cs.newLineAtOffset(margin, y - 2);
                cs.showText("Bangalore's Favorite Food Delivery");
                cs.endText();

                cs.beginText();
                cs.setNonStrokingColor(1f, 1f, 1f);
                cs.setFont(bold, 14);
                cs.newLineAtOffset(pageWidth - margin - 90, y + 10);
                cs.showText("INVOICE");
                cs.endText();

                y -= 100;

                // ---- Order meta info ----
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
                String placedAt = order.getPlacedAt() != null ? sdf.format(order.getPlacedAt()) : "-";

                y = writeLabelValueRow(cs, regular, bold, margin, y, "Invoice / Order #:", "TF-" + order.getOrderId());
                y = writeLabelValueRow(cs, regular, bold, margin, y, "Restaurant:", order.getRestaurantName());
                y = writeLabelValueRow(cs, regular, bold, margin, y, "Placed At:", placedAt);
                y = writeLabelValueRow(cs, regular, bold, margin, y, "Order Status:", order.getOrderStatus());
                y -= 10;

                y = writeLabelValueRow(cs, regular, bold, margin, y, "Customer:", order.getCustomerName());
                y = writeLabelValueRow(cs, regular, bold, margin, y, "Phone:", order.getCustomerPhone());
                y = writeLabelValueRow(cs, regular, bold, margin, y, "Delivery Address:", order.getDeliveryAddress());
                y -= 15;

                y = drawLine(cs, margin, pageWidth - margin, y);
                y -= 20;

                // ---- Items table header ----
                cs.beginText();
                cs.setNonStrokingColor(TEXT_MUTED);
                cs.setFont(bold, 10);
                cs.newLineAtOffset(margin, y);
                cs.showText("ITEM");
                cs.endText();
                cs.beginText();
                cs.setFont(bold, 10);
                cs.newLineAtOffset(pageWidth - margin - 220, y);
                cs.showText("QTY");
                cs.endText();
                cs.beginText();
                cs.setFont(bold, 10);
                cs.newLineAtOffset(pageWidth - margin - 150, y);
                cs.showText("PRICE");
                cs.endText();
                cs.beginText();
                cs.setFont(bold, 10);
                cs.newLineAtOffset(pageWidth - margin - 70, y);
                cs.showText("SUBTOTAL");
                cs.endText();

                y -= 8;
                y = drawLine(cs, margin, pageWidth - margin, y);
                y -= 20;

                // ---- Items rows ----
                for (OrderItem item : order.getOrderItems()) {
                    cs.beginText();
                    cs.setNonStrokingColor(TEXT_DARK);
                    cs.setFont(regular, 10.5f);
                    cs.newLineAtOffset(margin, y);
                    cs.showText(truncate(item.getItemName(), 40));
                    cs.endText();

                    cs.beginText();
                    cs.setFont(regular, 10.5f);
                    cs.newLineAtOffset(pageWidth - margin - 215, y);
                    cs.showText("x" + item.getQuantity());
                    cs.endText();

                    cs.beginText();
                    cs.setFont(regular, 10.5f);
                    cs.newLineAtOffset(pageWidth - margin - 150, y);
                    cs.showText(rupees(item.getPrice()));
                    cs.endText();

                    cs.beginText();
                    cs.setFont(regular, 10.5f);
                    cs.newLineAtOffset(pageWidth - margin - 70, y);
                    cs.showText(rupees(item.getSubtotal()));
                    cs.endText();

                    y -= 22;
                }

                y -= 5;
                y = drawLine(cs, margin, pageWidth - margin, y);
                y -= 25;

                // ---- Totals block (right aligned) ----
                y = writeTotalRow(cs, regular, margin, pageWidth, y, "Item Total", rupees(order.getItemTotal()), false);
                y = writeTotalRow(cs, regular, margin, pageWidth, y, "Platform Fee", rupees(order.getPlatformFee()), false);
                y = writeTotalRow(cs, regular, margin, pageWidth, y, "Delivery Fee", rupees(order.getDeliveryFee()), false);
                y = writeTotalRow(cs, regular, margin, pageWidth, y, "GST", rupees(order.getGst()), false);
                if (order.getCouponDiscount() > 0) {
                    y = writeTotalRow(cs, regular, margin, pageWidth, y,
                            "Coupon Discount" + (order.getCouponCode() != null ? " (" + order.getCouponCode() + ")" : ""),
                            "-" + rupees(order.getCouponDiscount()), false);
                }
                y -= 5;
                y = drawLine(cs, pageWidth - margin - 220, pageWidth - margin, y);
                y -= 20;
                y = writeTotalRow(cs, bold, margin, pageWidth, y, "FINAL TOTAL", rupees(order.getFinalTotal()), true);

                y -= 25;
                y = writeLabelValueRow(cs, regular, bold, margin, y, "Payment Method:", order.getPaymentMethod());
                y = writeLabelValueRow(cs, regular, bold, margin, y, "Payment Status:", order.getPaymentStatus());

                // ---- Footer ----
                cs.beginText();
                cs.setNonStrokingColor(TEXT_MUTED);
                cs.setFont(regular, 9.5f);
                cs.newLineAtOffset(margin, 60);
                cs.showText("Thank you for ordering with TummyFull! For support, contact support@tummyfull.com");
                cs.endText();
            }

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=TummyFull_Invoice_" + orderId + ".pdf");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            response.setContentLength(baos.size());
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
        }
    }

    private float writeLabelValueRow(PDPageContentStream cs, PDFont regular, PDFont bold,
                                      float x, float y, String label, String value) throws IOException {
        cs.beginText();
        cs.setNonStrokingColor(TEXT_MUTED);
        cs.setFont(bold, 10);
        cs.newLineAtOffset(x, y);
        cs.showText(label);
        cs.endText();

        cs.beginText();
        cs.setNonStrokingColor(TEXT_DARK);
        cs.setFont(regular, 10);
        cs.newLineAtOffset(x + 130, y);
        cs.showText(truncate(value == null ? "-" : value, 55));
        cs.endText();

        return y - 18;
    }

    private float writeTotalRow(PDPageContentStream cs, PDFont font, float margin, float pageWidth,
                                 float y, String label, String value, boolean emphasize) throws IOException {
        cs.beginText();
        cs.setNonStrokingColor(emphasize ? BRAND_DARK : TEXT_DARK);
        cs.setFont(font, emphasize ? 13 : 10.5f);
        cs.newLineAtOffset(pageWidth - margin - 220, y);
        cs.showText(label);
        cs.endText();

        cs.beginText();
        cs.setNonStrokingColor(emphasize ? BRAND_DARK : TEXT_DARK);
        cs.setFont(font, emphasize ? 13 : 10.5f);
        cs.newLineAtOffset(pageWidth - margin - 70, y);
        cs.showText(value);
        cs.endText();

        return y - (emphasize ? 22 : 18);
    }

    private float drawLine(PDPageContentStream cs, float x1, float x2, float y) throws IOException {
        cs.setStrokingColor(LINE_COLOR);
        cs.setLineWidth(0.75f);
        cs.moveTo(x1, y);
        cs.lineTo(x2, y);
        cs.stroke();
        return y;
    }

    private String rupees(double amount) {
        return String.format("Rs. %.2f", amount);
    }

    private String truncate(String s, int max) {
        if (s == null) return "-";
        return s.length() > max ? s.substring(0, max - 1) + "…" : s;
    }
}


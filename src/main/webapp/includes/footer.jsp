<%@ page contentType="text/html;charset=UTF-8" %>
<footer class="footer">
    <div class="container">
        <div class="footer-grid">
            <div>
                <div class="footer-brand"><span class="logo-badge">🍔</span> TummyFull</div>
                <p>Delicious food from Bangalore's best restaurants, delivered fast and fresh to your doorstep.</p>
                <div class="social-icons mt-2">
                    <a href="#" aria-label="Facebook">f</a>
                    <a href="#" aria-label="Instagram">i</a>
                    <a href="#" aria-label="Twitter">t</a>
                </div>
            </div>
            <div>
                <h4>Company</h4>
                <ul>
                    <li><a href="#">About Us</a></li>
                    <li><a href="#">Careers</a></li>
                    <li><a href="#">Blog</a></li>
                    <li><a href="#">Contact</a></li>
                </ul>
            </div>
            <div>
                <h4>For You</h4>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/RestaurantServlet">Restaurants</a></li>
                    <li><a href="${pageContext.request.contextPath}/OrderHistoryServlet">Your Orders</a></li>
                    <li><a href="${pageContext.request.contextPath}/agent-login.jsp">Become a Delivery Partner</a></li>
                </ul>
            </div>
            <div>
                <h4>Support</h4>
                <ul>
                    <li><a href="#">Help Center</a></li>
                    <li><a href="#">Terms of Service</a></li>
                    <li><a href="#">Privacy Policy</a></li>
                </ul>
            </div>
        </div>
        <div class="footer-bottom">
            <span>&copy; 2026 TummyFull. Serving Bangalore with love.</span>
            <span>Made for foodies, by foodies.</span>
        </div>
    </div>
</footer>

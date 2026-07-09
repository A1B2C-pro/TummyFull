<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tap.model.Restaurant" %>
<%@ page import="com.tap.dao.RestaurantDAO" %>
<%@ page import="com.tap.daoimpl.RestaurantDAOImpl" %>
<%
    RestaurantDAO restaurantDAO = new RestaurantDAOImpl();
    List<Restaurant> topRestaurants = restaurantDAO.getTopRatedRestaurants(8);
    request.setAttribute("topRestaurants", topRestaurants);
    boolean isLoggedIn = session.getAttribute("loggedInUser") != null;
    request.setAttribute("isLoggedIn", isLoggedIn);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TummyFull | Delicious Food, Delivered Fast in Bangalore</title>
    <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Ctext y='.9em' font-size='90'%3E%F0%9F%8D%94%3C/text%3E%3C/svg%3E">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shop.css">
</head>
<body>

<nav class="navbar">
    <div class="container navbar-inner">
        <a href="${pageContext.request.contextPath}/index.jsp" class="navbar-brand">
            <span class="logo-badge">🍔</span> TummyFull
        </a>
        <div class="navbar-links">
            <a href="#home">Home</a>
            <a href="${isLoggedIn ? pageContext.request.contextPath.concat('/RestaurantServlet') : pageContext.request.contextPath.concat('/login-role.jsp')}">Restaurants</a>
            <a href="#categories">Categories</a>
            <a href="#how-it-works">How it Works</a>
            <a href="#testimonials">Reviews</a>
        </div>
        <div class="navbar-actions">
            <c:choose>
                <c:when test="${isLoggedIn}">
                    <a href="${pageContext.request.contextPath}/RestaurantServlet" class="btn btn-dark btn-sm">My Account</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login-role.jsp" class="btn btn-dark btn-sm">Login</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</nav>

<section class="hero" id="home">
    <div class="container">
        <div class="hero-inner">
            <div>
                <span class="hero-badge">⭐ Bangalore's #1 Food Delivery</span>
                <h1>Delicious food<br>at your doorstep</h1>
                <p>Order from Bangalore's best-loved restaurants and get it delivered hot, fresh, and fast — right when you're craving it most.</p>
                <div class="hero-actions">
                    <a href="${pageContext.request.contextPath}/login-role.jsp" class="btn btn-primary">Get Started</a>
                    <a href="#how-it-works" class="btn btn-outline-light">How it Works</a>
                </div>
                <div class="hero-stats">
                    <div><div class="stat-num">500+</div><div class="stat-label">Restaurants</div></div>
                    <div><div class="stat-num">50k+</div><div class="stat-label">Happy Customers</div></div>
                    <div><div class="stat-num">4.8★</div><div class="stat-label">Average Rating</div></div>
                </div>
            </div>
            <div class="hero-visual">
                <img class="hero-food" src="https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600" alt="Delicious burger">
                <div class="hero-floating-card top">
                    <span class="stars">★★★★★</span> Top Rated Food
                </div>
                <div class="hero-floating-card bottom">
                    🚴 Delivered in 28 mins
                </div>
            </div>
        </div>
    </div>
</section>

<div class="container">
    <div class="promo-grid">
        <div class="promo-card yellow">
            <div>
                <div class="promo-tag">MADE FRESH • SERVED HOT</div>
                <h3>Irresistibly Tasty Meals</h3>
                <div class="promo-off">🔥 Up to 40%</div>
            </div>
            <img class="promo-img" src="https://images.unsplash.com/photo-1596797038530-2c107229654b?w=300" alt="Fresh meal">
        </div>
        <div class="promo-card coral">
            <div>
                <div class="promo-tag">LOADED BEEF BURGER</div>
                <h3>Weekend Special</h3>
                <div class="promo-off">Up to 30%</div>
            </div>
            <img class="promo-img" src="https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=300" alt="Burger">
        </div>
        <div class="promo-card green">
            <div>
                <div class="promo-tag">SUPER DELICIOUS</div>
                <h3>Pizza Bonanza</h3>
                <div class="promo-off">Up to 50%</div>
            </div>
            <img class="promo-img" src="https://images.unsplash.com/photo-1513104890138-7c749659a591?w=300" alt="Pizza">
        </div>
    </div>
</div>

<section class="section" id="popular-restaurants">
    <div class="container">
        <div class="section-head">
            <div>
                <h2>Popular Restaurants Near You</h2>
                <p class="text-muted mt-1">Our highest-rated picks from across Bangalore</p>
            </div>
            <a href="${isLoggedIn ? pageContext.request.contextPath.concat('/RestaurantServlet') : pageContext.request.contextPath.concat('/login-role.jsp')}" class="btn btn-dark">View All Restaurants</a>
        </div>
        <c:choose>
            <c:when test="${empty topRestaurants}">
                <p class="text-muted">No restaurants available yet — check back soon!</p>
            </c:when>
            <c:otherwise>
                <div class="grid">
                    <c:forEach var="r" items="${topRestaurants}">
                        <a href="${isLoggedIn ? pageContext.request.contextPath.concat('/MenuServlet?restaurantId=').concat(r.restaurantId) : pageContext.request.contextPath.concat('/login-role.jsp')}" class="card restaurant-card">
                            <div class="card-image-wrap">
                                <img src="${r.imageUrl}" alt="${r.name}">
                                <c:if test="${not empty r.offerText}"><span class="badge badge-offer">${r.offerText}</span></c:if>
                            </div>
                            <div class="card-body">
                                <div class="card-title">${r.name}</div>
                                <div class="card-sub">${r.cuisine}</div>
                                <div class="card-meta-row">
                                    <span class="rating-pill">★ ${r.rating}</span>
                                    <span>🕒 ${r.deliveryTime}</span>
                                </div>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<section class="section" id="categories">
    <div class="container">
        <div class="category-showcase">
            <div>
                <h2>Explore Delicious Meals by Category</h2>
                <p>From cheesy pizzas to spicy biryanis — find exactly what you're craving from our curated categories.</p>
                <a href="${pageContext.request.contextPath}/login-role.jsp" class="btn btn-dark">View All Categories</a>
            </div>
            <div class="category-tile-grid">
                <div class="category-tile">
                    <img src="https://images.unsplash.com/photo-1513104890138-7c749659a591?w=150" alt="Pizza">
                    <div class="cat-name">Pizza</div>
                    <div class="cat-count">120+ items</div>
                </div>
                <div class="category-tile">
                    <img src="https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=150" alt="Burgers">
                    <div class="cat-name">Burgers</div>
                    <div class="cat-count">95+ items</div>
                </div>
                <div class="category-tile">
                    <img src="https://images.unsplash.com/photo-1563379091339-03246963d96c?w=150" alt="Biryani">
                    <div class="cat-name">Biryani</div>
                    <div class="cat-count">80+ items</div>
                </div>
                <div class="category-tile">
                    <img src="https://images.unsplash.com/photo-1585032226651-759b368d7246?w=150" alt="Chinese">
                    <div class="cat-name">Chinese</div>
                    <div class="cat-count">70+ items</div>
                </div>
                <div class="category-tile">
                    <img src="https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=150" alt="South Indian">
                    <div class="cat-name">South Indian</div>
                    <div class="cat-count">60+ items</div>
                </div>
                <div class="category-tile">
                    <img src="https://images.unsplash.com/photo-1551024506-0bccd828d307?w=150" alt="Desserts">
                    <div class="cat-name">Desserts</div>
                    <div class="cat-count">55+ items</div>
                </div>
            </div>
        </div>
    </div>
</section>

<section class="section" id="how-it-works">
    <div class="container">
        <div class="section-head" style="justify-content:center;text-align:center;flex-direction:column;">
            <h2>How TummyFull Works</h2>
            <p class="text-muted mt-1">Get your favorite food in just four simple steps</p>
        </div>
        <div class="steps-grid">
            <div class="step-card">
                <div class="step-num">1</div>
                <h4>Choose a Restaurant</h4>
                <p>Browse hundreds of restaurants near you in Bangalore.</p>
            </div>
            <div class="step-card">
                <div class="step-num">2</div>
                <h4>Pick Your Food</h4>
                <p>Select your favorite dishes and add them to your cart.</p>
            </div>
            <div class="step-card">
                <div class="step-num">3</div>
                <h4>Pay Securely</h4>
                <p>Choose from COD, UPI, or card and place your order.</p>
            </div>
            <div class="step-card">
                <div class="step-num">4</div>
                <h4>Track &amp; Enjoy</h4>
                <p>Track your order live until it reaches your doorstep.</p>
            </div>
        </div>
    </div>
</section>

<section class="section" id="testimonials" style="background:var(--color-mint);border-radius:40px;">
    <div class="container">
        <div class="section-head" style="justify-content:center;text-align:center;flex-direction:column;">
            <h2>What Our Customers Say</h2>
        </div>
        <div class="grid">
            <div class="testimonial-card">
                <div class="stars">★★★★★</div>
                <p class="quote">"TummyFull has the fastest delivery I've experienced in Bangalore. Food always arrives hot!"</p>
                <div class="testimonial-user">
                    <img src="https://images.unsplash.com/photo-1607746882042-944635dfe10e?w=100" alt="">
                    <div><div class="name">Aditi Sharma</div><div class="role">Indiranagar</div></div>
                </div>
            </div>
            <div class="testimonial-card">
                <div class="stars">★★★★★</div>
                <p class="quote">"Great variety of restaurants and the offers are genuinely useful. My go-to app now."</p>
                <div class="testimonial-user">
                    <img src="https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100" alt="">
                    <div><div class="name">Rohit Verma</div><div class="role">Koramangala</div></div>
                </div>
            </div>
            <div class="testimonial-card">
                <div class="stars">★★★★★</div>
                <p class="quote">"Live order tracking is a game changer. I always know exactly when my food will arrive."</p>
                <div class="testimonial-user">
                    <img src="https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=100" alt="">
                    <div><div class="name">Sneha Reddy</div><div class="role">HSR Layout</div></div>
                </div>
            </div>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="cta-banner">
            <h2>Hungry Already?</h2>
            <p>Join thousands of happy foodies across Bangalore ordering from TummyFull every day.</p>
            <a href="${pageContext.request.contextPath}/login-role.jsp" class="btn btn-primary">Order Now</a>
        </div>
    </div>
</section>

<jsp:include page="/includes/footer.jsp"/>

</body>
</html>
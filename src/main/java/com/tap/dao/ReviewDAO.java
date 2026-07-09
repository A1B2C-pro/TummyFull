package com.tap.dao;

import com.tap.model.Review;
import java.util.List;

public interface ReviewDAO {
    List<Review> getReviewsByRestaurant(int restaurantId);
    List<Review> getReviewsByMenu(int menuId);
    List<Review> getAllReviews();
    List<Review> getHighlightedReviews();
    boolean addReview(Review review);
    boolean deleteReview(int reviewId);
    boolean highlightReview(int reviewId, boolean highlight);
}

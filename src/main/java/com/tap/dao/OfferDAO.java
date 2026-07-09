package com.tap.dao;

import com.tap.model.Offer;
import java.util.List;

public interface OfferDAO {
    List<Offer> getAllOffers();
    List<Offer> getActiveOffers();
    boolean addOffer(Offer offer);
    boolean updateOffer(Offer offer);
    boolean deleteOffer(int offerId);
}

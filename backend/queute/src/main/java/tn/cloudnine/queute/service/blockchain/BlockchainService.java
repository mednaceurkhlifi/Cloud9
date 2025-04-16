package tn.cloudnine.queute.service.blockchain;

import tn.cloudnine.queute.model.booking.Booking;

public interface BlockchainService {
    boolean createBookingOnBlockchain(Booking booking);
    boolean updateBookingOnBlockchain(Booking booking);
    boolean deleteBookingOnBlockchain(String reference);
    boolean verifyBookingOnBlockchain(String reference);
    
    // New methods
    String getContractAddress();
    String getNetworkName();
} 
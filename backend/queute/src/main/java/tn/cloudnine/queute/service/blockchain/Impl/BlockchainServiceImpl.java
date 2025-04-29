package tn.cloudnine.queute.service.blockchain.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.StaticGasProvider;
import tn.cloudnine.queute.blockchain.BookingContract;
import tn.cloudnine.queute.config.BlockchainConfig;
import tn.cloudnine.queute.model.booking.Booking;
import tn.cloudnine.queute.service.blockchain.BlockchainService;
import tn.cloudnine.queute.util.BlockchainUtils;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlockchainServiceImpl implements BlockchainService {

    private final Web3j web3j;
    private final Credentials credentials;
    private final BlockchainConfig blockchainConfig;
    private final StaticGasProvider gasProvider;
    
    @Value("${blockchain.retry.max-attempts:3}")
    private int maxRetryAttempts;
    
    @Value("${blockchain.retry.delay-seconds:5}")
    private int retryDelaySeconds;

    private BookingContract getContract() {
        return BookingContract.load(
            blockchainConfig.getContractAddress(),
            web3j,
            credentials,
            gasProvider
        );
    }

    @Override
    public boolean createBookingOnBlockchain(Booking booking) {
        try {
            BookingContract contract = getContract();
            
            CompletableFuture<TransactionReceipt> future = BlockchainUtils.executeWithRetry(
                () -> contract.createBooking(
                    booking.getReference(),
                    booking.getStatus(),
                    BigInteger.valueOf(booking.getUser().getUserId()),
                    BigInteger.valueOf(booking.getServices().getServiceId())
                ).sendAsync(),
                maxRetryAttempts,
                retryDelaySeconds,
                "Create booking on blockchain"
            );
            
            return true;
        } catch (Exception e) {
            // log.error("Error creating booking on blockchain: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateBookingOnBlockchain(Booking booking) {
        try {
            BookingContract contract = getContract();
            
            CompletableFuture<TransactionReceipt> future = BlockchainUtils.executeWithRetry(
                () -> contract.updateBooking(
                    booking.getReference(),
                    booking.getStatus()
                ).sendAsync(),
                maxRetryAttempts,
                retryDelaySeconds,
                "Update booking on blockchain"
            );
            
            return true;
        } catch (Exception e) {
            // log.error("Error updating booking on blockchain: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteBookingOnBlockchain(String reference) {
        try {
            BookingContract contract = getContract();
            
            CompletableFuture<TransactionReceipt> future = BlockchainUtils.executeWithRetry(
                () -> contract.deleteBooking(reference).sendAsync(),
                maxRetryAttempts,
                retryDelaySeconds,
                "Delete booking on blockchain"
            );
            
            return true;
        } catch (Exception e) {
            // log.error("Error deleting booking on blockchain: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean verifyBookingOnBlockchain(String reference) {
        try {
            BookingContract contract = getContract();
            return contract.verifyBooking(reference).send();
        } catch (Exception e) {
            // log.error("Error verifying booking on blockchain: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String getContractAddress() {
        return blockchainConfig.getContractAddress();
    }
    
    @Override
    public String getNetworkName() {
        try {
            long chainId = web3j.ethChainId().send().getChainId().longValue();
            return getNetworkNameFromChainId(chainId);
        } catch (Exception e) {
            // log.error("Error getting network name: {}", e.getMessage());
            return "Unknown Network";
        }
    }
    
    private String getNetworkNameFromChainId(long chainId) {
        switch ((int) chainId) {
            case 1:
                return "Ethereum Mainnet";
            case 3:
                return "Ropsten Testnet";
            case 4:
                return "Rinkeby Testnet";
            case 5:
                return "Goerli Testnet";
            case 42:
                return "Kovan Testnet";
            case 56:
                return "Binance Smart Chain";
            case 137:
                return "Polygon";
            case 1337:
                return "Local Development Network";
            default:
                return "Unknown Network (Chain ID: " + chainId + ")";
        }
    }
} 
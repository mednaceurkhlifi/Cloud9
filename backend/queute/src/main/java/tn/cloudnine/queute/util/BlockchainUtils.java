package tn.cloudnine.queute.util;

import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class BlockchainUtils {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Execute a blockchain operation with retries
     * @param operation The operation to execute
     * @param maxRetries Maximum number of retries
     * @param delaySeconds Delay between retries in seconds
     * @param operationName Name of the operation for logging
     * @return CompletableFuture with the result
     */
    public static CompletableFuture<TransactionReceipt> executeWithRetry(
            Supplier<CompletableFuture<TransactionReceipt>> operation,
            int maxRetries,
            int delaySeconds,
            String operationName) {
        
        CompletableFuture<TransactionReceipt> resultFuture = new CompletableFuture<>();
        
        executeWithRetryInternal(operation, maxRetries, delaySeconds, operationName, 0, resultFuture);
        
        return resultFuture;
    }
    
    private static void executeWithRetryInternal(
            Supplier<CompletableFuture<TransactionReceipt>> operation,
            int maxRetries,
            int delaySeconds,
            String operationName,
            int currentRetry,
            CompletableFuture<TransactionReceipt> resultFuture) {
        
        try {
            operation.get().thenAccept(receipt -> {
                log.info("{} completed successfully with transaction hash: {}", 
                        operationName, receipt.getTransactionHash());
                resultFuture.complete(receipt);
            }).exceptionally(ex -> {
                handleOperationFailure(operation, maxRetries, delaySeconds, operationName, 
                        currentRetry, resultFuture, ex);
                return null;
            });
        } catch (Exception e) {
            handleOperationFailure(operation, maxRetries, delaySeconds, operationName, 
                    currentRetry, resultFuture, e);
        }
    }
    
    private static void handleOperationFailure(
            Supplier<CompletableFuture<TransactionReceipt>> operation,
            int maxRetries,
            int delaySeconds,
            String operationName,
            int currentRetry,
            CompletableFuture<TransactionReceipt> resultFuture,
            Throwable ex) {
        
        if (currentRetry < maxRetries) {
            int nextRetry = currentRetry + 1;
            log.warn("{} failed (attempt {}/{}), will retry in {} seconds. Error: {}", 
                    operationName, nextRetry, maxRetries, delaySeconds, ex.getMessage());
            
            scheduler.schedule(() -> 
                executeWithRetryInternal(operation, maxRetries, delaySeconds, 
                        operationName, nextRetry, resultFuture), 
                delaySeconds, TimeUnit.SECONDS);
        } else {
            log.error("{} failed after {} attempts. Error: {}", 
                    operationName, maxRetries, ex.getMessage());
            resultFuture.completeExceptionally(ex);
        }
    }
    
    /**
     * Shutdown the scheduler
     */
    public static void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
} 
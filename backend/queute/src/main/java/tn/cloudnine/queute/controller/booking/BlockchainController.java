package tn.cloudnine.queute.controller.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.codegen.SolidityFunctionWrapperGenerator;
import org.web3j.tx.Contract;
import tn.cloudnine.queute.model.booking.Booking;
import tn.cloudnine.queute.repository.booking.BookingRepository;
import tn.cloudnine.queute.service.blockchain.BlockchainService;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blockchain")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class BlockchainController {
    private final BlockchainService blockchainService;
    private final BookingRepository bookingRepository;

    @GetMapping("/verify/all")
    public ResponseEntity<Map<String, Object>> verifyAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        Map<String, Boolean> verificationResults = new HashMap<>();
        int verified = 0;
        int failed = 0;

        for (Booking booking : bookings) {
            boolean result = blockchainService.verifyBookingOnBlockchain(booking.getReference());
            verificationResults.put(booking.getReference(), result);
            if (result) {
                verified++;
            } else {
                failed++;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalBookings", bookings.size());
        response.put("verifiedBookings", verified);
        response.put("failedBookings", failed);
        response.put("details", verificationResults);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify/{reference}")
    public ResponseEntity<Booking> verifyBooking(@PathVariable String reference) {
        Booking booking = bookingRepository.findBookingByReference(reference);

        boolean result = blockchainService.verifyBookingOnBlockchain(booking.getReference());
        return ResponseEntity.ok(booking);
    }


    @PostMapping("/generate-wrapper")
    public ResponseEntity<Map<String, String>> generateContractWrapper() {
        try {
            // Define the paths
            String basePath = System.getProperty("user.dir");
            String contractsPath = Paths.get(basePath, "src", "main", "resources", "contracts").toString();
            String outputPath = Paths.get(basePath, "src", "main", "java").toString();
            String packageName = "tn.cloudnine.queute.blockchain";

            // Generate the wrapper
            String[] args = {
                "-a", Paths.get(contractsPath, "BookingContract.abi").toString(),
                "-b", Paths.get(contractsPath, "BookingContract.bin").toString(),
                "-o", outputPath,
                "-p", packageName
            };
            
            SolidityFunctionWrapperGenerator.main(args);
            
            // Return success response
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Contract wrapper generated successfully",
                "outputPath", outputPath + "/tn/cloudnine/queute/blockchain/BookingContract.java"
            ));
        } catch (Exception e) {
            log.error("Error generating contract wrapper: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error",
                "message", "Error generating contract wrapper: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/contract-info")
    public ResponseEntity<Map<String, String>> getContractInfo() {
        try {
            return ResponseEntity.ok(Map.of(
                "contractName", "BookingContract",
                "contractAddress", blockchainService.getContractAddress(),
                "blockchain", "Ethereum",
                "network", blockchainService.getNetworkName()
            ));
        } catch (Exception e) {
            log.error("Error getting contract info: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error",
                "message", "Error getting contract info: " + e.getMessage()
            ));
        }
    }
} 
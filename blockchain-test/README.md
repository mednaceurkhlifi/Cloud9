# Blockchain Testing Environment for BookingContract

This project provides a local Ethereum blockchain environment for testing the BookingContract with Hardhat.

## Prerequisites

- Node.js (v14+)
- npm or yarn

## Setup and Usage

1. Install dependencies:
   ```
   npm install
   ```

2. Start a local Hardhat blockchain node:
   ```
   npx hardhat node
   ```
   
   This will start a local blockchain node at http://localhost:8545 and display a list of accounts with their private keys.
   **Important:** Copy one of the private keys to use in your application.properties

3. Open a new terminal and deploy the BookingContract:
   ```
   npx hardhat run scripts/deploy.js --network localhost
   ```
   
   This will deploy the contract and output the contract address. Copy this address to use in your application.properties

4. Update your application.properties with the blockchain configuration:
   ```
   blockchain.contract.address=<CONTRACT_ADDRESS> # from step 3
   blockchain.web3j.client-address=http://localhost:8545
   blockchain.private-key=<PRIVATE_KEY> # from step 2 (without 0x prefix)
   ```

## Testing BookingContract Functions

You can interact with the deployed contract using the Hardhat console:

```
npx hardhat console --network localhost
```

Example commands:
```javascript
const BookingContract = await ethers.getContractFactory("BookingContract");
const contract = await BookingContract.attach("<CONTRACT_ADDRESS>");

// Create a booking
await contract.createBooking("REF123", "CONFIRMED", 1, 2);

// Verify a booking
const verified = await contract.verifyBooking("REF123");
console.log("Booking verified:", verified);

// Get booking details
const bookingDetails = await contract.getBooking("REF123");
console.log("Booking details:", bookingDetails);

// Update a booking
await contract.updateBooking("REF123", "COMPLETED");

// Delete a booking
await contract.deleteBooking("REF123");
```

## Integration with Your Application

Once the contract is deployed and the application.properties file is updated, your Java application will interact with this local blockchain instance.

## Troubleshooting

1. **Reset the local blockchain:** If you want to start from scratch, stop the Hardhat node and restart it.

2. **Nonce errors:** If you encounter nonce errors, it means the transaction count is out of sync. Reset the Hardhat node.

3. **Gas errors:** The default gas limit should be sufficient, but if needed, adjust the gas settings in your application.properties. 
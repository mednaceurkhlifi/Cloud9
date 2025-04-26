// Script to deploy the BookingContract to the local Hardhat network
const { ethers } = require("hardhat");

async function main() {
  console.log("Deploying BookingContract...");

  // Get the contract factory
  const BookingContract = await ethers.getContractFactory("BookingContract");
  
  // Deploy the contract
  const bookingContract = await BookingContract.deploy();
  
  // Wait for deployment to finish
  await bookingContract.waitForDeployment();
  
  // Get the deployed contract address
  const contractAddress = await bookingContract.getAddress();
  
  console.log("BookingContract deployed to:", contractAddress);
  console.log("Use this address in your application.properties file");
  
  // Get the deployer's address
  const [deployer] = await ethers.getSigners();
  console.log("Deployer account:", deployer.address);
  console.log("Private key is available in your Hardhat node logs/config");
}

// Execute the deployment
main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error("Error during deployment:", error);
    process.exit(1);
  }); 
const { expect } = require("chai");
const { ethers } = require("hardhat");

describe("BookingContract", function () {
  let bookingContract;
  let owner;
  let user1;
  let user2;
  
  const bookingRef = "BOOKING123";
  const initialStatus = "CONFIRMED";
  const updatedStatus = "COMPLETED";
  const userId = 1;
  const serviceId = 2;

  beforeEach(async function () {
    // Get signers
    [owner, user1, user2] = await ethers.getSigners();

    // Deploy the contract
    const BookingContract = await ethers.getContractFactory("BookingContract");
    bookingContract = await BookingContract.deploy();
    await bookingContract.waitForDeployment();
  });

  it("Should create a new booking", async function () {
    // Create a booking
    await bookingContract.createBooking(bookingRef, initialStatus, userId, serviceId);
    
    // Verify the booking exists
    const verified = await bookingContract.verifyBooking(bookingRef);
    expect(verified).to.equal(true);
  });

  it("Should retrieve booking details", async function () {
    // Create a booking
    await bookingContract.createBooking(bookingRef, initialStatus, userId, serviceId);
    
    // Get booking details
    const booking = await bookingContract.getBooking(bookingRef);
    
    // Check the returned data
    expect(booking[0]).to.equal(bookingRef);
    expect(booking[1]).to.equal(initialStatus);
    expect(booking[2]).to.equal(userId);
    expect(booking[3]).to.equal(serviceId);
    // Timestamps (booking[4] and booking[5]) are dynamic, so we don't check them
  });

  it("Should update a booking status", async function () {
    // Create a booking
    await bookingContract.createBooking(bookingRef, initialStatus, userId, serviceId);
    
    // Update the booking
    await bookingContract.updateBooking(bookingRef, updatedStatus);
    
    // Get the updated booking
    const booking = await bookingContract.getBooking(bookingRef);
    
    // Check the status was updated
    expect(booking[1]).to.equal(updatedStatus);
  });

  it("Should delete a booking", async function () {
    // Create a booking
    await bookingContract.createBooking(bookingRef, initialStatus, userId, serviceId);
    
    // Delete the booking
    await bookingContract.deleteBooking(bookingRef);
    
    // Verify the booking no longer exists
    const verified = await bookingContract.verifyBooking(bookingRef);
    expect(verified).to.equal(false);
    
    // Trying to get a deleted booking should fail
    await expect(
      bookingContract.getBooking(bookingRef)
    ).to.be.revertedWith("Booking was deleted");
  });

  it("Should fail if creating a booking with an existing reference", async function () {
    // Create a booking
    await bookingContract.createBooking(bookingRef, initialStatus, userId, serviceId);
    
    // Try to create another booking with the same reference
    await expect(
      bookingContract.createBooking(bookingRef, initialStatus, userId, serviceId)
    ).to.be.revertedWith("Booking already exists");
  });

  it("Should fail when updating a non-existent booking", async function () {
    // Try to update a booking that doesn't exist
    await expect(
      bookingContract.updateBooking("NONEXISTENT", updatedStatus)
    ).to.be.revertedWith("Booking does not exist");
  });

  it("Should fail when deleting a non-existent booking", async function () {
    // Try to delete a booking that doesn't exist
    await expect(
      bookingContract.deleteBooking("NONEXISTENT")
    ).to.be.revertedWith("Booking does not exist");
  });
}); 
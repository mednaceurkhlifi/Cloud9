// SPDX-License-Identifier: MIT
pragma solidity 0.5.0;

contract BookingContract {
    struct Booking {
        string ref;
        string status;
        uint256 userId;
        uint256 serviceId;
        uint256 createdAt;
        uint256 updatedAt;
        bool exists;
    }

    mapping(string => Booking) private bookings;
    mapping(string => bool) private deletedBookings;

    event BookingCreated(string ref, string status, uint256 userId, uint256 serviceId);
    event BookingUpdated(string ref, string status);
    event BookingDeleted(string ref);

    function createBooking(
        string memory ref,
        string memory status,
        uint256 userId,
        uint256 serviceId
    ) public returns (bool) {
        require(!bookings[ref].exists, "Booking already exists");
        require(!deletedBookings[ref], "Booking was deleted");

        bookings[ref] = Booking({
            ref: ref,
            status: status,
            userId: userId,
            serviceId: serviceId,
            createdAt: block.timestamp,
            updatedAt: block.timestamp,
            exists: true
        });

        emit BookingCreated(ref, status, userId, serviceId);
        return true;
    }

    function getBooking(string memory ref) public view returns (
        string memory,
        string memory,
        uint256,
        uint256,
        uint256,
        uint256
    ) {
        require(bookings[ref].exists, "Booking does not exist");
        require(!deletedBookings[ref], "Booking was deleted");

        Booking memory booking = bookings[ref];
        return (
            booking.ref,
            booking.status,
            booking.userId,
            booking.serviceId,
            booking.createdAt,
            booking.updatedAt
        );
    }

    function updateBooking(
        string memory ref,
        string memory status
    ) public returns (bool) {
        require(bookings[ref].exists, "Booking does not exist");
        require(!deletedBookings[ref], "Booking was deleted");

        bookings[ref].status = status;
        bookings[ref].updatedAt = block.timestamp;

        emit BookingUpdated(ref, status);
        return true;
    }

    function deleteBooking(string memory ref) public returns (bool) {
        require(bookings[ref].exists, "Booking does not exist");
        require(!deletedBookings[ref], "Booking was already deleted");

        deletedBookings[ref] = true;
        emit BookingDeleted(ref);
        return true;
    }

    function verifyBooking(string memory ref) public view returns (bool) {
        return bookings[ref].exists && !deletedBookings[ref];
    }
} 
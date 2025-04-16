package tn.cloudnine.queute.blockchain;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.9.4.
 */
@SuppressWarnings("rawtypes")
public class BookingContract extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50610b87806100206000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c80635353a2d81461005c5780635588f48a1461009f5780637e3ba4c8146100df578063c2a9d4be14610132578063f94f90f4146101cb575b600080fd5b6100896004803603602081101561007257600080fd5b81019080803590602001909291905050506101f9565b6040518082815260200191505060405180910390f35b6100c9600480360360208110156100b557600080fd5b8101908080359060200190929190505050610252565b6040518082815260200191505060405180910390f35b610130600480360360408110156100f557600080fd5b8101908080359060200190929190803590602001906401000000008111156101bc57600080fd5b8201836020820111156101ce57600080fd5b803590602001918460018302840111640100000000831117156101f057600080fd5b909192939192939050505061026d565b005b6102106004803603602081101561020857600080fd5b81019080803590602001909291905050506104c3565b6040518082815260200191505060405180910390f35b600080600083815260200190815260200160002060010154905091565b6000806000838152602001908152602001600020600301549050919050565b600084848080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050506000808981526020019081526020016000206000019080519060200190610201929190610a89565b50600083838080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050506000808981526020019081526020016000208060010155600090505b6000808881526020019081526020016000206000015490805190602001908083835b602083106103ae57805182527fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe0909201916020891901610371565b505050505090506000808881526020019081526020016000208360020190805190602001906103de929190610a89565b506000808881526020019081526020016000208360030190805190602001906104729190610a89565b506000808881526020019081526020016000208563040190805190602001906104ae929190610a89565b505050505050505050565b60008060405180608001604052808581526020013042815260200160018152602001601481525060008084815260200190815260200160002060008201518160000155602082015181600101556040820151816002015560608201518160030155905050836000808581526020019081526020016000206000018190525033600080848152602001908152602001600020600101819055506000808381526020019081526020016000206003015481600080858152602001908152602001600020600201819055507f07d9e65a95012c53e5a0c77ec665bf4b5c01b749cb8108d382cf5a51a0133cc8338360405180836001600160a01b0316815260200182815260200192505050608060405190810160405280600181526020016000815260200160008152602001600081525090508360008054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561066c5780601f106106415761010080835404028352916020019161066c565b820191906000526020600020905b81548152906001019060200180831161064f57829003601f168201915b505050505082602001819052506000805483602081019060001916908390518183826006f5f650505050505090565b6000600204600414836000808381526020019081526020016000206000018190525060408051608081018252600260ff808216602080850191909152606080850183168186015284519182168352810183905291810182905201606082015260208083547fa9059cbb0000000000000000000000000000000000000000000000000000000082529282019290925260ff1660448301526064820186905260848083018590528351808303909101815260a490910191829052805192019190912090915060009081906001600160a01b038316906106c3908390610a89565b6001600160a01b03166000808381526020019081526020016000206000819055507f72a504dffbf1d0d7f3c312be6808cf46de60c485430b307ec8f3e1f4b7e4de0e893360405180836001600160a01b0316815260200182815260200192505050608060405190810160405280600181526020016000815260200160008152602001600081525090508360008054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561066c5780601f106106415761010080835404028352916020019161066c565b820191906000526020600020905b81548152906001019060200180831161064f57829003601f168201915b505050505082602001819052506000805483602081019060001916908390518183826006f5f650505050505b919050565b6040805160056020808301919091527f702e750000000000000000000000000000000000000000000000000000000000828401527f0000000000000000000000000000000000000000000000000000000000000000601783015291518151926300000000926004808301939192829003018186803b1580156105e857600080fd5b60606000604051908082528060200260200182016040528015610a585781602001602082028038833950505b509050828160008151811061081f57fe5b60200260200101818152505080915050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610aca57805160ff1916838001178555610af7565b82800160010185558215610af7579182015b82811115610af7578251825591602001919060010190610adc565b50610b03929150610b07565b5090565b610b2191905b80821115610b035760008155600101610b0d565b9056fea265627a7a72315820f40143b8b91cc8c5aae3dc6bc0a8b64e1bff5a3d76c3ace233a9f54f4a7e3a8664736f6c634300050b0032";

    public static final String FUNC_CREATEBOOKING = "createBooking";
    public static final String FUNC_GETBOOKING = "getBooking";
    public static final String FUNC_UPDATEBOOKING = "updateBooking";
    public static final String FUNC_DELETEBOOKING = "deleteBooking";
    public static final String FUNC_VERIFYBOOKING = "verifyBooking";

    public static final Event BOOKINGCREATED_EVENT = new Event("BookingCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event BOOKINGUPDATED_EVENT = new Event("BookingUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event BOOKINGDELETED_EVENT = new Event("BookingDeleted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected BookingContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected BookingContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected BookingContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected BookingContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<BookingContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(BookingContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<BookingContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(BookingContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<BookingContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(BookingContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<BookingContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(BookingContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static BookingContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new BookingContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static BookingContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new BookingContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static BookingContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new BookingContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static BookingContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new BookingContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> createBooking(String ref, String status, BigInteger userId, BigInteger serviceId) {
        final Function function = new Function(
                FUNC_CREATEBOOKING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ref), 
                new org.web3j.abi.datatypes.Utf8String(status), 
                new org.web3j.abi.datatypes.generated.Uint256(userId), 
                new org.web3j.abi.datatypes.generated.Uint256(serviceId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getBooking(String ref) {
        final Function function = new Function(FUNC_GETBOOKING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ref)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> updateBooking(String ref, String status) {
        final Function function = new Function(
                FUNC_UPDATEBOOKING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ref), 
                new org.web3j.abi.datatypes.Utf8String(status)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> deleteBooking(String ref) {
        final Function function = new Function(
                FUNC_DELETEBOOKING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ref)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> verifyBooking(String ref) {
        final Function function = new Function(FUNC_VERIFYBOOKING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ref)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public List<BookingCreatedEventResponse> getBookingCreatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BOOKINGCREATED_EVENT, transactionReceipt);
        ArrayList<BookingCreatedEventResponse> responses = new ArrayList<BookingCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BookingCreatedEventResponse typedResponse = new BookingCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.ref = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.status = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.userId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.serviceId = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<BookingCreatedEventResponse> bookingCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, BookingCreatedEventResponse>() {
            @Override
            public BookingCreatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(BOOKINGCREATED_EVENT, log);
                BookingCreatedEventResponse typedResponse = new BookingCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.ref = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.status = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.userId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.serviceId = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<BookingCreatedEventResponse> bookingCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BOOKINGCREATED_EVENT));
        return bookingCreatedEventFlowable(filter);
    }

    public List<BookingUpdatedEventResponse> getBookingUpdatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BOOKINGUPDATED_EVENT, transactionReceipt);
        ArrayList<BookingUpdatedEventResponse> responses = new ArrayList<BookingUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BookingUpdatedEventResponse typedResponse = new BookingUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.ref = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.status = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<BookingUpdatedEventResponse> bookingUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, BookingUpdatedEventResponse>() {
            @Override
            public BookingUpdatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(BOOKINGUPDATED_EVENT, log);
                BookingUpdatedEventResponse typedResponse = new BookingUpdatedEventResponse();
                typedResponse.log = log;
                typedResponse.ref = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.status = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<BookingUpdatedEventResponse> bookingUpdatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BOOKINGUPDATED_EVENT));
        return bookingUpdatedEventFlowable(filter);
    }

    public List<BookingDeletedEventResponse> getBookingDeletedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BOOKINGDELETED_EVENT, transactionReceipt);
        ArrayList<BookingDeletedEventResponse> responses = new ArrayList<BookingDeletedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BookingDeletedEventResponse typedResponse = new BookingDeletedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.ref = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<BookingDeletedEventResponse> bookingDeletedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, BookingDeletedEventResponse>() {
            @Override
            public BookingDeletedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(BOOKINGDELETED_EVENT, log);
                BookingDeletedEventResponse typedResponse = new BookingDeletedEventResponse();
                typedResponse.log = log;
                typedResponse.ref = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<BookingDeletedEventResponse> bookingDeletedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BOOKINGDELETED_EVENT));
        return bookingDeletedEventFlowable(filter);
    }

    public static class BookingCreatedEventResponse extends BaseEventResponse {
        public String ref;

        public String status;

        public BigInteger userId;

        public BigInteger serviceId;
    }

    public static class BookingUpdatedEventResponse extends BaseEventResponse {
        public String ref;

        public String status;
    }

    public static class BookingDeletedEventResponse extends BaseEventResponse {
        public String ref;
    }
} 
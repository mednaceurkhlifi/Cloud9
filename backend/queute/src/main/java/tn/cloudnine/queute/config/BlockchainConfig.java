package tn.cloudnine.queute.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import java.math.BigInteger;

@Configuration
@Getter
public class BlockchainConfig {

    @Value("${blockchain.web3j.client-address:http://localhost:8545}")
    private String web3jClientAddress;

    @Value("${blockchain.contract.address}")
    private String contractAddress;

    @Value("${blockchain.private-key}")
    private String privateKey;
    
    @Value("${blockchain.gas-price:20000000000}")
    private String gasPrice;
    
    @Value("${blockchain.gas-limit:4712388}")
    private String gasLimit;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(web3jClientAddress));
    }

    @Bean
    public Credentials credentials() {
        return Credentials.create(privateKey);
    }
    
    @Bean
    public StaticGasProvider gasProvider() {
        return new StaticGasProvider(
            new BigInteger(gasPrice), 
            new BigInteger(gasLimit)
        );
    }
} 
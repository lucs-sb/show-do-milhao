package api.showdomilhao.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfiguration {
    @Value("${amazon.s3.region}")
    private String REGION;

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard().withRegion(REGION)
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
    }
}

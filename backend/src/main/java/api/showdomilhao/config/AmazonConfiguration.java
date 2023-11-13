package api.showdomilhao.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfiguration {
    @Value("${amazon.s3.acess.key}")
    private final String ACCESS_KEY="[Minha chave de acesso]";
    @Value("${amazon.s3.secret.key}")
    private final String SECRET_KEY="[Minha chave de segredo]";
    @Value("${amazon.s3.region}")
    private final String REGION="us-east-1";

    @Bean
    public BasicAWSCredentials basicAWSCredentials(){
        return new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
    }

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard().withRegion(REGION)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials())).build();
    }
}

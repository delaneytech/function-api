package io.theground.functionapi

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableAutoConfiguration(exclude = [ContextStackAutoConfiguration::class])
class S3SinkConfiguration {

    @Value("\${cloud.aws.credentials.access-key}")
    private val accessKey: String? = null

    @Value("\${cloud.aws.credentials.secret-key}")
    private val secretKey: String? = null

    @Value("\${cloud.aws.region.static}")
    private val region: String? = null

    @Bean
    fun basicAWSCredentials(): BasicAWSCredentials {
        return BasicAWSCredentials(accessKey, secretKey)
    }

    @Bean
    fun amazonS3(awsCredentials: AWSCredentials) : AmazonS3 {
        val amazonS3Client = AmazonS3Client(awsCredentials)
        amazonS3Client.setRegion(Region.getRegion(Regions.fromName(region)))
        return amazonS3Client
    }


}
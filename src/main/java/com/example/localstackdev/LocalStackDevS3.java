package com.example.localstackdev;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.net.URI;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class LocalStackDevS3 {

    public static final String LOCALSTACK_ENDPOINT_URL = "http://127.0.0.1:4566";
    public static final Region AWS_REGION = Region.US_EAST_1;
    public static final String AWS_ACCESS_KEY_ID = "testLocal";
    public static final String AWS_SECRET_ACCESS_KEY = "testLocal";

    public static S3Client createLocalS3Client() {
        System.out.println("Creating a locals3client using LOCALSTACK_ENDPOINT_URL = " + LOCALSTACK_ENDPOINT_URL + ", AWS_REGION = " + AWS_REGION + ", AWS_ACCESS_KEY_ID = " + AWS_ACCESS_KEY_ID + ", AWS_SECRET_ACCESS_KEY = " + AWS_SECRET_ACCESS_KEY);
        return S3Client.builder()
                .endpointOverride(URI.create(LOCALSTACK_ENDPOINT_URL))
                .region(AWS_REGION)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY)))
                .build();
    }

    public static void createLocalBucket(S3Client s3Client, String bucketName) {
        System.out.println("Creating local bucket using s3Client = " + s3Client + ", bucketName = " + bucketName);
        s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
    }

    public static void deleteLocalBucket(S3Client s3Client, String bucketName) {
        System.out.println("Deleting local bucket using s3Client = " + s3Client + ", bucketName = " + bucketName);
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucketName).build();
        ListObjectsV2Response listObjectsV2Response;

        System.out.println("Clearing the bucket, bucketName = " + bucketName);
        do {
            listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);
            System.out.println("Objects in bucket: " + listObjectsV2Response.contents());
            for (S3Object s3Object : listObjectsV2Response.contents()) {
                System.out.println("Deleting: " + s3Object);
                s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(s3Object.key()).build());
            }

            listObjectsV2Request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .continuationToken(listObjectsV2Response.nextContinuationToken())
                    .build();
        } while (listObjectsV2Response.isTruncated());

        System.out.println("Deleting the bucket, bucketName = " + bucketName);
        s3Client.deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build());
    }

    public static void generateTestData() {
        try (S3Client s3Client = createLocalS3Client()) {
            String bucketName1 = "my-local-bucket-1";
            String bucketName2 = "my-local-bucket-2";

            // Delete bucket if it exists
            try {
                System.out.println("Attempting to delete existing bucket: " + bucketName1);
                deleteLocalBucket(s3Client, bucketName1);
            } catch (Exception e) {
                // Ignore if bucket doesn't exist
                System.out.println("Bucket " + bucketName1 + " does not exist. Skipping deletion.");
            }

            // Create bucket
            System.out.println("Creating bucket: " + bucketName1);
            createLocalBucket(s3Client, bucketName1);

            // Delete bucket if it exists
            try {
                System.out.println("Attempting to delete existing bucket: " + bucketName2);
                deleteLocalBucket(s3Client, bucketName2);
            } catch (Exception e) {
                // Ignore if bucket doesn't exist
                System.out.println("Bucket " + bucketName2 + " does not exist. Skipping deletion.");
            }

            // Create bucket
            System.out.println("Creating bucket: " + bucketName2);
            createLocalBucket(s3Client, bucketName2);

            String[] headers = {
                    "bzAccountNumber", "bankReference", "bankBookDate", "payeeAccountNumber",
                    "payeeName", "brokerCode", "paymentAmount", "currencyType", "exchangeRate",
                    "policyNumber"
            };

            Random random = new Random();
            LocalDate startDate = LocalDate.of(2020, 1, 1);
            LocalDate endDate = LocalDate.of(2023, 12, 31);

            // Generate 5 files with 20 records each in the pending folder
            System.out.println("Generating files...");
            for (int i = 1; i <= 5; i++) {
                StringBuilder csvData = new StringBuilder(String.join(",", headers) + "\n");
                for (int j = 1; j <= 20; j++) {
                    csvData.append(randomString(10, random)).append(",")  // bzAccountNumber
                            .append(randomString(12, random)).append(",")  // bankReference
                            .append(randomDate(startDate, endDate, random)).append(",")  // bankBookDate
                            .append(randomString(10, random)).append(",")  // payeeAccountNumber
                            .append(randomString(8, random)).append(",")  // payeeName
                            .append(randomString(6, random)).append(",")  // brokerCode
                            .append(String.format("%.2f", random.nextDouble() * (10000 - 100) + 100)).append(",")  // paymentAmount
                            .append(randomCurrencyType(random)).append(",")  // currencyType
                            .append(String.format("%.4f", random.nextDouble() * (1.5 - 0.5) + 0.5)).append(",")  // exchangeRate
                            .append(randomString(15, random)).append("\n");  // policyNumber
                }

                String key = "payment-file-" + i + ".csv";
                System.out.println("Creating file: " + key);
                s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName1)
                    .key(key)
                    .build(), RequestBody.fromString(csvData.toString()));
            }
        }
    }

    private static String randomString(int length, Random random) {
        return random.ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private static String randomDate(LocalDate start, LocalDate end, Random random) {
        long days = ChronoUnit.DAYS.between(start, end);
        return start.plusDays(random.nextInt((int) days + 1)).toString();
    }

    private static String randomCurrencyType(Random random) {
        String[] currencies = {"USD", "EUR", "GBP"};
        return currencies[random.nextInt(currencies.length)];
    }
}

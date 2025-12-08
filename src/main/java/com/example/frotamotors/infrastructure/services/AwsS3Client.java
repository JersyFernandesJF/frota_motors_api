package com.example.frotamotors.infrastructure.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class AwsS3Client {

  private final S3Client s3Client;

  @Value("${spring.s3.bucket.name:frota-motors-bucket}")
  private String bucketName;

  @Value("${spring.s3.region:us-east-1}")
  private String region;

  @Value("${spring.s3.endpoint:}")
  private String endpoint;

  public AwsS3Client(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  /**
   * Upload a file to S3 with key starting with "frotamotors"
   *
   * @param file The file to upload
   * @param identifier Identifier (e.g., vehicleId, userId) to include in the key
   * @return The public URL of the uploaded file
   * @throws IOException If upload fails
   */
  public String uploadFile(MultipartFile file, String identifier) throws IOException {
    // Validate file size (max 10MB)
    if (file.getSize() > 10 * 1024 * 1024) {
      throw new IOException("File size exceeds maximum allowed size of 10MB");
    }

    // Validate file type (only images)
    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new IOException("Only image files are allowed. Received: " + contentType);
    }

    // Validate specific image types
    String[] allowedTypes = {"image/jpeg", "image/jpg", "image/png", "image/webp"};
    boolean isAllowed = false;
    for (String allowedType : allowedTypes) {
      if (contentType.equalsIgnoreCase(allowedType)) {
        isAllowed = true;
        break;
      }
    }
    if (!isAllowed) {
      throw new IOException("Image type not allowed. Allowed types: jpeg, jpg, png, webp");
    }
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    // Get file extension
    String originalFilename = file.getOriginalFilename();
    String extension = "";
    if (originalFilename != null && originalFilename.contains(".")) {
      extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    // Key format: frotamotors_{identifier}_{timestamp}_{random}{extension}
    // UUID ensures concurrent uploads within the same second never collide.
    String uniqueSuffix = UUID.randomUUID().toString().replace("-", "");
    String key =
        String.format(
            "frotamotors_%s_%s_%s%s", identifier, now.format(formatter), uniqueSuffix, extension);

    // Compress image before upload
    CompressedImageResult compressionResult = compressImage(file);
    InputStream compressedImageStream = compressionResult.getInputStream();
    long compressedSize = compressionResult.getSize();

    PutObjectRequest putObjectRequest =
        PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .acl(ObjectCannedACL.PUBLIC_READ)
            .contentType(file.getContentType())
            .build();

    s3Client.putObject(
        putObjectRequest, RequestBody.fromInputStream(compressedImageStream, compressedSize));

    return getFileUrl(key);
  }

  /** Result class for compressed image */
  private static class CompressedImageResult {
    private final InputStream inputStream;
    private final long size;

    public CompressedImageResult(InputStream inputStream, long size) {
      this.inputStream = inputStream;
      this.size = size;
    }

    public InputStream getInputStream() {
      return inputStream;
    }

    public long getSize() {
      return size;
    }
  }

  /**
   * Compress image to reduce file size while maintaining quality Max width: 1920px, Quality: 85%
   *
   * @param file Original image file
   * @return Compressed image result with InputStream and size
   * @throws IOException If compression fails
   */
  private CompressedImageResult compressImage(MultipartFile file) throws IOException {
    try {
      // Read original image
      var originalImage = ImageIO.read(file.getInputStream());
      if (originalImage == null) {
        // If not a valid image, return original
        return new CompressedImageResult(file.getInputStream(), file.getSize());
      }

      int originalWidth = originalImage.getWidth();
      int originalHeight = originalImage.getHeight();
      int maxWidth = 1920;
      int maxHeight = 1920;

      // Only compress if image is larger than max dimensions
      if (originalWidth <= maxWidth && originalHeight <= maxHeight && file.getSize() < 500 * 1024) {
        // Image is already small enough, return original
        return new CompressedImageResult(file.getInputStream(), file.getSize());
      }

      // Calculate new dimensions maintaining aspect ratio
      double scale =
          Math.min((double) maxWidth / originalWidth, (double) maxHeight / originalHeight);
      int newWidth = (int) (originalWidth * scale);
      int newHeight = (int) (originalHeight * scale);

      // Compress image
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      Thumbnails.of(originalImage)
          .size(newWidth, newHeight)
          .outputFormat(getImageFormat(file.getContentType()))
          .outputQuality(0.85)
          .toOutputStream(outputStream);

      byte[] compressedBytes = outputStream.toByteArray();
      return new CompressedImageResult(
          new ByteArrayInputStream(compressedBytes), compressedBytes.length);
    } catch (IOException | RuntimeException e) {
      // If compression fails, return original file
      return new CompressedImageResult(file.getInputStream(), file.getSize());
    }
  }

  /**
   * Get image format from content type
   *
   * @param contentType MIME type
   * @return Image format string for ImageIO
   */
  private String getImageFormat(String contentType) {
    if (contentType == null) {
      return "jpg";
    }
    if (contentType.contains("png")) {
      return "png";
    }
    if (contentType.contains("webp")) {
      return "webp";
    }
    // Default to jpg for jpeg/jpg
    return "jpg";
  }

  /**
   * Delete a file from S3
   *
   * @param key The S3 key of the file to delete
   */
  public void deleteFile(String key) {
    DeleteObjectRequest deleteObjectRequest =
        DeleteObjectRequest.builder().bucket(bucketName).key(key).build();
    s3Client.deleteObject(deleteObjectRequest);
  }

  /**
   * Extract S3 key from a full URL
   *
   * @param url The full S3 URL
   * @return The S3 key or null if URL is invalid
   */
  public String extractKeyFromUrl(String url) {
    if (url == null || url.isEmpty()) {
      return null;
    }

    // Handle AWS S3 URL format: https://bucket.s3.region.amazonaws.com/key
    if (url.contains(".s3.") && url.contains(".amazonaws.com/")) {
      int keyStart = url.indexOf(".amazonaws.com/") + ".amazonaws.com/".length();
      return url.substring(keyStart);
    }

    // Handle LocalStack URL format: http://localhost:4566/bucket/key
    if (url.contains("/" + bucketName + "/")) {
      int keyStart = url.indexOf("/" + bucketName + "/") + ("/" + bucketName + "/").length();
      return url.substring(keyStart);
    }

    // If URL is just a key (starts with frotamotors)
    if (url.startsWith("frotamotors_")) {
      return url;
    }

    return null;
  }

  /**
   * Get the public URL for a file in S3
   *
   * @param key The S3 key
   * @return The public URL
   */
  public String getFileUrl(String key) {
    // Se usar LocalStack (endpoint customizado), retorna URL do LocalStack
    if (endpoint != null && !endpoint.isEmpty()) {
      // LocalStack usa path-style: http://localhost:4566/bucket-name/key
      String baseUrl = endpoint.replace("localstack", "localhost"); // Para acesso externo
      return String.format("%s/%s/%s", baseUrl, bucketName, key);
    }
    // URL padrão AWS
    return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
  }
}

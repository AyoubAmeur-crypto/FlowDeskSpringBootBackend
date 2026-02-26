package spring.tuto.flowdesk.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Upload image to Cloudinary
     * @param file MultipartFile from form
     * @return URL of uploaded image
     */
    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Upload to Cloudinary with custom options
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "services",  // Organize images in folders
                        "public_id", "service_" + UUID.randomUUID(),  // Unique filename
                        "overwrite", true,
                        "resource_type", "image"
                ));

        return (String) uploadResult.get("secure_url");
    }

    /**
     * Upload image with transformation (resize, crop, etc.)
     */
    public String uploadImageWithTransformation(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "services",
                        "public_id", "service_" + UUID.randomUUID(),
                        "transformation", ObjectUtils.asMap(
                                "width", 800,
                                "height", 600,
                                "crop", "limit",  // Don't upscale, only downscale if needed
                                "quality", "auto"
                        )
                ));

        return (String) uploadResult.get("secure_url");
    }

    /**
     * Delete image from Cloudinary
     * @param imageUrl Full URL of the image
     */
    public void deleteImage(String imageUrl) throws IOException {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        // Extract public_id from URL
        String publicId = extractPublicIdFromUrl(imageUrl);

        if (publicId != null) {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
    }

    /**
     * Extract public_id from Cloudinary URL
     */
    private String extractPublicIdFromUrl(String url) {
        try {
            // URL format: https://res.cloudinary.com/cloud_name/image/upload/v123456/folder/public_id.jpg
            String[] parts = url.split("/");
            if (parts.length > 0) {
                // Get the last part and remove extension
                String fileNameWithExt = parts[parts.length - 1];
                String fileName = fileNameWithExt.substring(0, fileNameWithExt.lastIndexOf('.'));

                // If in a folder, include folder path
                if (parts.length > 8) {
                    String folder = parts[parts.length - 2];
                    return folder + "/" + fileName;
                }
                return fileName;
            }
        } catch (Exception e) {
            // If extraction fails, return null
        }
        return null;
    }
}

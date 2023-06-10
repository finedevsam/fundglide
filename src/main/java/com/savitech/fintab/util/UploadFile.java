package com.savitech.fintab.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UploadFile {

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;
    public Pair<Boolean, String> uploadCloudinary(MultipartFile file){
        try {
            Cloudinary cloudinary = new Cloudinary(cloudinaryConfig());
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), cloudinaryOption("fintab"));

            return Pair.of(true, uploadResult.get("url").toString());
        }catch (IOException e){
            return Pair.of(false, e.getMessage());
        }
    }


    public  String uploadImageToCloudinary(File qrCodeFile) {
        try {
            // Convert BufferedImage to byte array
            Cloudinary cloudinary = new Cloudinary(cloudinaryConfig());

            Map<String, Object> uploadResult = cloudinary.uploader().upload(qrCodeFile, cloudinaryOption("qrcode"));

            // Print the public URL of the uploaded image
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private Map<String, String> cloudinaryConfig(){

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);

        return config;
    }


    private static Map<String, String> cloudinaryOption(String folder){;

        Map<String, String> options = new HashMap<>();
        options.put("folder", folder);
        options.put("use_filename", "true");
        options.put("unique_filename", "true");
        options.put("overwrite", "false");

        return options;
    }
}

package com.savitech.fintab.util;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        try {
            Map<String, String> options = new HashMap<>();
            options.put("folder", "fintab");
            options.put("use_filename", "true");
            options.put("unique_filename", "true");
            options.put("overwrite", "false");

            Cloudinary cloudinary = new Cloudinary(config);
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

            return Pair.of(true, uploadResult.get("url").toString());
        }catch (IOException e){
            return Pair.of(false, e.getMessage());
        }
    }
}

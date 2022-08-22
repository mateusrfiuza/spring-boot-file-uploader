package com.example.file_uploader.entrypoint.http.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Set;

import org.apache.logging.log4j.util.Strings;
import org.springframework.web.multipart.MultipartFile;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private static final Set<String> ALLOWED_FILE_EXTENSIONS = Set.of("application/pdf", "image/png", "image/jpg", "image/jpeg");

    @Override
    public boolean isValid(final MultipartFile multipartFile, final ConstraintValidatorContext context) {

        boolean result = true;

        final var contentType = multipartFile.getContentType();
        if (Strings.isNotEmpty(contentType) && (!isSupportedContentType(contentType))) {
            result = false;
        }

        return result;
    }

    private boolean isSupportedContentType(final String contentType) {
        return ALLOWED_FILE_EXTENSIONS.contains(contentType);
    }

}
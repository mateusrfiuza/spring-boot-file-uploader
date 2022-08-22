package com.example.file_uploader.entrypoint.http;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Size;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.example.file_uploader.entrypoint.http.validator.ValidFile;
import com.example.file_uploader.service.FileCompressorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@Validated
@RequestMapping("/files")
public class FilesResource {

    private static final Logger logger = LoggerFactory.getLogger(FilesResource.class);

    private final FileCompressorService service;
    private final MultipartFilesToFileDataSetConverter converter;

    public FilesResource(final FileCompressorService service, final MultipartFilesToFileDataSetConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @PostMapping(value = "/compact", consumes = "multipart/form-data")
        @Operation(description = "Given some valid files, it will transform into a Zip file")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Request ok and your files will be compressed"),
                @ApiResponse(responseCode = "400", description = "Your parameters are invalid"),
                @ApiResponse(responseCode = "413", description = "Exceeded upload file size"),
                @ApiResponse(responseCode = "500", description = "Some internal error happened"),
        })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<StreamingResponseBody> compact(
            @RequestPart
            @Size(min = 1,
                  max = 5,
                  message = "The number of files must be between {min} and {max}"
            )
            final List<@ValidFile MultipartFile> files,
            final HttpServletResponse response) {

        logger.debug("Received {} files", files.size());

        final var fileDataSet = converter.convert(files);

        final StreamingResponseBody streamResponseBody = outputStream -> service.execute(
                fileDataSet,
                response.getOutputStream()
        );

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=export.zip")
                .body(streamResponseBody);
    }


}

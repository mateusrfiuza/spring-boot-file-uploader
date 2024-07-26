package com.example.file_uploader.entrypoint.http;

import com.example.file_uploader.service.FileCompressorService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilesResource.class)
class FilesResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileCompressorService service;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    void should_return_200_when_receive_valid_files() throws Exception {

        /* Given */
        final var validMockFile = createValidFile();

        /* When */
        final var result = mockMvc.perform(
                multipart("/files/compact")
                        .file(validMockFile)
                        .file(validMockFile)
        );

        /* Then */
        result
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment;filename=export.zip"));
    }


    @Test
    void should_return_400_when_receive_invalid_files_extensions() throws Exception {

        /* Given */
        final var validMockFile = createValidFile();
        final var invalidMockFile = createInvalidFile();

        /* When */
        final var result = mockMvc.perform(
                multipart("/files/compact")
                        .file(validMockFile)
                        .file(invalidMockFile)
        );

        /* Then */
        result.andExpect(status().isBadRequest())
              .andExpect(header().string("Content-Type", "application/json"))
              .andExpect(jsonPath("$.message", contains("Only PDF, PNG or JPG images are allowed")));
    }

    @Test
    void should_return_400_when_exceed_allowed_files_amount() throws Exception {

        /* Given */
        final var validMockFile = createValidFile();

        /* When */
        final var result = mockMvc.perform(
                multipart("/files/compact")
                        .file(validMockFile)
                        .file(validMockFile)
                        .file(validMockFile)
                        .file(validMockFile)
                        .file(validMockFile)
                        .file(validMockFile)
        );

        /* Then */
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", contains("The number of files must be between 1 and 5")));
    }

    @Test
    void should_return_400_when_exceed_allowed_files_amount_and_invalid_files() throws Exception {

        /* Given */
        final var validMockFile = createValidFile();

        final var invalidMockFile = createInvalidFile();


        /* When */
        final var result = mockMvc.perform(
                multipart("/files/compact")
                        .file(validMockFile)
                        .file(validMockFile)
                        .file(validMockFile)
                        .file(validMockFile)
                        .file(validMockFile)
                        .file(invalidMockFile)
        );

        /* Then */
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                                    contains(
                                            "Only PDF, PNG or JPG images are allowed",
                                            "The number of files must be between 1 and 5")));
    }

    private MockMultipartFile createValidFile() throws IOException {
        return new MockMultipartFile(
                "files",
                "validFile",
                MediaType.APPLICATION_PDF_VALUE,
                resourceLoader.getResource("classpath:valid_file.pdf")
                              .getInputStream()
        );
    }

    private MockMultipartFile createInvalidFile() throws IOException {
        return new MockMultipartFile(
                "files",
                "invalidFile",
                MediaType.TEXT_XML_VALUE,
                resourceLoader.getResource("classpath:invalid_file.xml")
                              .getInputStream()
        );
    }

}
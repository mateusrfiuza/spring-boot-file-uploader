package com.example.file_uploader.entrypoint.http;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.file_uploader.service.FileData;
import com.example.file_uploader.service.exception.InvalidFileException;

@Component
public class MultipartFilesToFileDataSetConverter implements Converter<List<MultipartFile>, Set<FileData>> {

    @Override
    public Set<FileData> convert(final List<MultipartFile> source) {

        return source.stream()
                     .map(multipartFile -> {
                         try {
                             return new FileData(
                                     multipartFile.getOriginalFilename(),
                                     multipartFile.getInputStream()
                                                  .readAllBytes()
                             );
                         } catch (IOException e) {
                             throw new InvalidFileException("Error reading file" + multipartFile.getOriginalFilename());
                         }

                     })
                     .collect(Collectors.toSet());

    }


}

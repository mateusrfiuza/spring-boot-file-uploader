package com.example.file_uploader.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.file_uploader.service.exception.InvalidFileException;


@Service
public class FileCompressorServiceImpl implements FileCompressorService {

    private static final Logger logger = LoggerFactory.getLogger(FileCompressorServiceImpl.class);

    @Override
    public ZipOutputStream execute(final Set<FileData> files, final OutputStream outputStream) {
        final var zipOutputStream = new ZipOutputStream(outputStream);

        files.forEach(fileData -> {
            try {
                zipOutputStream.putNextEntry(new ZipEntry(fileData.name()));
                zipOutputStream.write(fileData.value());
            } catch (IOException e) {
                final var messageError = "Error compressing file" + fileData.name();
                logger.error(messageError);
                throw new InvalidFileException(messageError);
            } finally {
                try {
                    zipOutputStream.finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return zipOutputStream;

    }


}

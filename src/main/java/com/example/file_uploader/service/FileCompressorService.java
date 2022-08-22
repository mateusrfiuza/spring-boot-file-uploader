package com.example.file_uploader.service;

import java.io.OutputStream;
import java.util.Set;
import java.util.zip.ZipOutputStream;


public interface FileCompressorService {

    ZipOutputStream execute(final Set<FileData> files, final OutputStream outputStream);

}

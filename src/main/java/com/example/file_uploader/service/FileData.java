package com.example.file_uploader.service;

import java.util.Arrays;
import java.util.Objects;

public record FileData(String name, byte[] value) {

    public FileData {
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return "FileData{" +
                "name='" + name + '\'' +
                ", valueSize=" + value.length +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileData file = (FileData) o;
        return name.equals(file.name) && Arrays.equals(value, file.value);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(value);
        return result;
    }

}


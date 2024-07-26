package com.example.file_uploader.entrypoint.http.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileValidatorTest {

  @Mock
  private ConstraintValidatorContext context;

  @Mock
  private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

  @InjectMocks
  private FileValidator fileValidator;


  @Test
  public void testShouldReturnTrueWhenIsValidFile() {
    MultipartFile file = new MockMultipartFile("file", "valid_file.pdf", "application/pdf", "content".getBytes());
    assertTrue(fileValidator.isValid(file, context));
  }

  @Test
  public void testShouldReturnFalseWhenReceiveEmptyFile() {
    when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
    MultipartFile file = new MockMultipartFile("file", "valid_file.pdf", "application/pdf", new byte[0]);
    assertFalse(fileValidator.isValid(file, context));
  }

  @Test
  public void testShouldReturnFalseWhenReceiveNullFile() {
    when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
    assertFalse(fileValidator.isValid(null, context));
  }

  @Test
  public void testShouldReturnFalseWhenReceiveInvalidFileFormat() {
    MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());
    assertFalse(fileValidator.isValid(file, context));
  }


}
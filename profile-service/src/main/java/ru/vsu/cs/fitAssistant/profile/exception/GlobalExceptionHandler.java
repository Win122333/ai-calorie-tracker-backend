package ru.vsu.cs.fitAssistant.profile.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import ru.vsu.cs.dto.ErrorResponseDto;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("Данные не прошли прескоринг: {}", e.getMessage());
        String errors = e.getFieldErrors().stream()
                .map(x -> x.getField().concat(" ").concat(x.getDefaultMessage()))
                .collect(Collectors.joining("; "));

        ErrorResponseDto response = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpClientErrorException(HttpClientErrorException e) {
        log.warn("Неправильно введенные данные: {}", e.getMessage());

        try {
            ErrorResponseDto errorBody = objectMapper.readValue(
                    e.getResponseBodyAsString(),
                    ErrorResponseDto.class
            );
            return ResponseEntity.status(e.getStatusCode()).body(errorBody);
        }
        catch (Exception ex) {
            ErrorResponseDto response = new ErrorResponseDto(
                    e.getStatusCode().value(),
                    "Ошибка валидации"
            );
            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }
}

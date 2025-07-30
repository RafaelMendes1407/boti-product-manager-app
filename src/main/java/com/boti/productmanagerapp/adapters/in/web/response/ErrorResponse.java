package com.boti.productmanagerapp.adapters.in.web.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp; // Quando o erro ocorreu
    private Integer status;          // Código de status HTTP (ex: 409, 500)
    private String error;            // Nome do status HTTP (ex: "Conflict", "Internal Server Error")
    private String message;          // Mensagem detalhada do erro
    private String path;             // O endpoint que foi chamado (URL da requisição)

    public ErrorResponse(HttpStatus status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }
}
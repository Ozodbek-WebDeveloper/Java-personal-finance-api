package com.example.personal_finance_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.example.personal_finance_api.enums.TransactionType;

public record CategoryReq(

    @NotBlank(message = "name bo'sh bo'lmasligi kerak!") String name,

    @NotNull(message = "Category type tanlanishi shart!") TransactionType type

) {

}

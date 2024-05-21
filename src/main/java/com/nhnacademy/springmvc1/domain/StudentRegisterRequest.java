package com.nhnacademy.springmvc1.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import lombok.Value;

@Value
public class StudentRegisterRequest {
    @NonNull
    String id;
    @NonNull
    String pwd;
    @Pattern(regexp = "^\\S+$", message = "이름은 공백없이 1글자 이상 입력하세요.")
    String name;
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "이메일 형식이 아닙니다.")
    String email;
    @Min(value = 0, message = "점수의 최소값은 0입니다.")
    @Max(value = 100, message = "점수의 최대값은 100입니다.")
    int score;
    @Pattern(regexp = "^\\S+$", message = "평가는 공백없이 1글자 이상 입력하세요.")
    @Size(max = 200, message = "평가는 200자 이하여야 합니다.")
    String rating;
}
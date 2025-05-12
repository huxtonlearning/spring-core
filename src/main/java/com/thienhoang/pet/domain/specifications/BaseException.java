package com.thienhoang.pet.domain.specifications;

import com.thienhoang.pet.domain.specifications.models.values.enums.BaseErrorMessage;
import com.thienhoang.pet.domain.utils.KeywordReplacer;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
  HttpStatus status;
  String messageCode;

  public BaseException(HttpStatus status, BaseErrorMessage msg) {
    super(msg.val());
    this.status = status;
    messageCode = msg.toString();
  }

  public BaseException(HttpStatus status, BaseErrorMessage msg, Map<String, String> data) {

    super(KeywordReplacer.replaceKeywords(msg.val(), data));
    this.status = status;
    messageCode = msg.toString();
  }

  public BaseException(HttpStatus status, String msg) {
    super(msg);
    this.status = status;
  }
}

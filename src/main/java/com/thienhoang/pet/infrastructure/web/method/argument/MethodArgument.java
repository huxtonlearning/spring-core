package com.thienhoang.pet.infrastructure.web.method.argument;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Getter
@Component
public class MethodArgument {

  public final List<IMethodArgument> methodArgumentList;
}

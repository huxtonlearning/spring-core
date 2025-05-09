package com.thienhoang.pet.domain.specifications.api;

import com.thienhoang.pet.domain.models.values.response.PageResponse;
import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import com.thienhoang.pet.domain.specifications.services.IGetAllService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;

public interface IGetAllController<E, RES, P> {

  default IGetAllService<E, RES, P> getGetAllService() {

    return null;
  }

  @GetMapping
  @Parameters({
    @Parameter(
        name = "filter",
        in = ParameterIn.QUERY,
        description =
            "JSON string chứa object filter. Ví dụ: {\"status\":\"ACTIVE\", \"category\":\"BOOK\"}",
        schema = @Schema(type = "string", format = "json"))
  })
  default PageResponse<RES> getAll(
      @Parameter(hidden = true) HeaderContext context,
      Pageable pageable,
      @Parameter(hidden = true) P params) {

    if (getGetAllService() == null) {
      return null;
    }

    return new PageResponse<>(getGetAllService().getAll(context, pageable, params));
  }
}

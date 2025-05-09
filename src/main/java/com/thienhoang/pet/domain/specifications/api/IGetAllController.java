package com.thienhoang.pet.domain.specifications.api;

import com.thienhoang.pet.domain.models.values.response.PageResponse;
import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import com.thienhoang.pet.domain.specifications.services.IGetAllService;
import com.thienhoang.pet.domain.utils.ParamsKeys;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface IGetAllController<E, RES, P> {

  default IGetAllService<E, RES, P> getGetAllService() {

    return null;
  }

  @GetMapping
  @Parameters({
    @Parameter(
        name = ParamsKeys.FILTER,
        in = ParameterIn.QUERY,
        description =
            "JSON string chứa object filter. Ví dụ: {\"status\":\"ACTIVE\", \"category\":\"BOOK\"}",
        schema = @Schema(type = "string", format = "json")),
    @Parameter(
        name = "page",
        in = ParameterIn.QUERY,
        description = "Số trang (bắt đầu từ 0)",
        schema = @Schema(type = "integer", defaultValue = "0")),
    @Parameter(
        name = "page_size",
        in = ParameterIn.QUERY,
        description = "Kích thước trang",
        schema = @Schema(type = "integer", defaultValue = "20")),
    @Parameter(
        name = "sort",
        in = ParameterIn.QUERY,
        description = "JSON dạng: {\"createdAt\": -1, \"updatedAt\": 1} (1 = ASC, -1 = DESC)",
        required = false,
        schema =
            @Schema(
                type = "string",
                format = "json",
                example = "{\"createdAt\": -1, \"updatedAt\": 1}"))
  })
  default PageResponse<RES> getAll(
      @Parameter(hidden = true) HeaderContext context,
      @RequestParam(required = false) String search,
      @Parameter(hidden = true) Pageable pageable,
      @Parameter(hidden = true) P params) {

    if (getGetAllService() == null) {
      return null;
    }

    return new PageResponse<>(getGetAllService().getAll(context, search, pageable, params));
  }
}

package com.springboot.lococo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content; // 💡 추가
import io.swagger.v3.oas.models.media.MediaType; // 💡 추가
import io.swagger.v3.oas.models.media.Schema; // 💡 추가
import io.swagger.v3.oas.models.parameters.RequestBody; // 💡 추가
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer; // 💡 추가
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map; // 💡 추가

@Configuration
public class SwaggerConfig {

    private final String AUTH_TOKEN_HEADER = "Authorization";

    private Info apiInfo() {
        return new Info()
                .title("Swagger API")
                .description("Swagger API 테스트")
                .version("1.0.0");
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement().addList(AUTH_TOKEN_HEADER))
                .components(new Components()
                        .addSecuritySchemes(AUTH_TOKEN_HEADER, new SecurityScheme()
                                .name(AUTH_TOKEN_HEADER)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                );
    }

    @Bean
    public OpenApiCustomizer customizer() {
        // 커스터마이징을 수행할 OpenApiCustomizer 빈 정의
        return openApi -> openApi.getPaths().values().stream()
                .flatMap(pathItem -> pathItem.readOperations().stream())
                // 요청 엔드포인트와 HTTP 메서드가 일치하는지 확인
                .filter(operation -> operation.getOperationId() != null &&
                        operation.getOperationId().equals("createContent")) // OperationId는 Springdoc이 자동으로 생성하는 메서드 이름과 일치하는지 확인해야 합니다.
                .forEach(operation -> {
                    // DTO 스키마 정의를 위해 $ref를 사용하고, 없으면 새로 추가
                    final String dtoSchemaName = "ContentCreateDto";

                    // 1. RequestBody 스키마 정의
                    Schema<?> multipartSchema = new Schema<>()
                            .type("object")
                            .addProperty("dto", new Schema<>()
                                    .description("콘텐츠 생성에 필요한 JSON 데이터")
                                    .example("{\"title\": \"제목\", \"description\": \"내용\"}") // 예시 JSON 문자열
                                    // 실제 DTO 구조를 보여주려면 $ref를 사용
                                    .$ref("#/components/schemas/" + dtoSchemaName)
                            )
                            .addProperty("imageFile", new Schema<>()
                                    .type("string")
                                    .format("binary") // 파일 타입 (Swagger UI에 파일 선택 버튼 생성)
                                    .description("업로드할 이미지 파일")
                            );

                    // 2. RequestBody 객체 생성
                    RequestBody requestBody = new RequestBody()
                            .required(true)
                            .content(new Content()
                                    .addMediaType(org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE, // "multipart/form-data" 미디어 타입 지정
                                            new MediaType().schema(multipartSchema)
                                    )
                            );

                    // 3. Operation에 RequestBody 적용
                    operation.setRequestBody(requestBody);

                    // 4. ContentCreateDto 스키마가 Components에 없으면 추가 (Optional)
                    // Springdoc이 자동으로 DTO를 인식하면 이 부분은 생략 가능하지만, 명시적으로 추가하는 것이 안전합니다.
                    if (openApi.getComponents() != null && openApi.getComponents().getSchemas() != null
                            && !openApi.getComponents().getSchemas().containsKey(dtoSchemaName)) {
                        // TODO: ContentCreateDto의 실제 스키마 구조를 여기에 정의해야 합니다.
                        // 예시: openApi.getComponents().addSchemas(dtoSchemaName, new Schema<>().type("object").addProperty("title", new Schema<>().type("string")));
                        // Springdoc을 사용한다면 @Schema, @ArraySchema 등으로 DTO에 애너테이션을 붙여서 자동 생성하는 것이 더 효율적입니다.
                    }
                });
    }


}


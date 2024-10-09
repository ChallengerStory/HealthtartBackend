
package com.dev5ops.healthtart.routine.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.type.DateTime;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class RoutineDTO {
    @JsonProperty("routine_code")
    private Long routineCode;

    @JsonProperty("title")
    private String title;

    @JsonProperty("time")
    private DateTime time;

    @JsonProperty("link")
    private String link;

    @JsonProperty("recommend_music")
    private String recommend_music;

    @JsonProperty("created_at")
    private DateTime createdAt;

    @JsonProperty("updated_at")
    private DateTime updatedAt;
}

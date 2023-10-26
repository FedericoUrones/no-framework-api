package org.api.noframework.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewTask {
    private String description;
}
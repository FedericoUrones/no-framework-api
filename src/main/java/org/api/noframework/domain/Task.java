package org.api.noframework.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Task {

    String id;
    String description;
}
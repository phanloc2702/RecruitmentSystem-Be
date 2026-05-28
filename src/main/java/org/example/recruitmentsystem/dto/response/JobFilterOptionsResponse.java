package org.example.recruitmentsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class JobFilterOptionsResponse {

    private List<String> locations;

    private List<String> employmentTypes;

    private List<String> experienceLevels;
}
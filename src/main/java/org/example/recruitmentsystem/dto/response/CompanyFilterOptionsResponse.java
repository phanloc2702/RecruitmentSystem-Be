package org.example.recruitmentsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CompanyFilterOptionsResponse {

    private List<String> industries;

    private List<String> locations;
}
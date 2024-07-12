package example.sequence.issue.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Period {
    private int index;
    private String name;
}

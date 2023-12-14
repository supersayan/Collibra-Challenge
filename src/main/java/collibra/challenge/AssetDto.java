package collibra.challenge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class AssetDto {
    private Integer id;

    private String name;

    private Integer parentId;

    private Boolean isPromoted;
}


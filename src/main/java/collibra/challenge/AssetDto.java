package collibra.challenge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AssetDto {
    private Integer id;

    private String name;

    private Boolean isPromoted;

    private AssetDto parent;
}


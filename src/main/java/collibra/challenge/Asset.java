package collibra.challenge;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor

@Entity
@Document(collection = "assets")
public class Asset {
    @Id
    private Integer id;

    private String name;

    private boolean isPromoted;

    private Integer parentId;

    @Transient
    private Asset parent;

    @Transient
    private List<Integer> childrenIds;

    @Transient
    private List<Asset> children;
}


package collibra.challenge;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotNull
    private String name;

    @Column(name = "is_promoted")
    @NotNull
    private Boolean isPromoted;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "id"))
    private Asset parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asset> children;

    // public void setIsPromoted(Boolean isPromoted) {
    //     this.isPromoted = isPromoted;
    //     // rabbitTemplate.send("asset.promoted", null);
    //     for (Asset childAsset : children) {
    //         if (childAsset.getIsPromoted() != isPromoted)
    //             childAsset.setIsPromoted(isPromoted);
    //     }
    //     if (parent != null && parent.getIsPromoted() != isPromoted) {
    //         parent.setIsPromoted(isPromoted);
    //     }
    // }

    @Override
    public String toString() {
        return name + ": " + Integer.toString(id) + (isPromoted ? " PROMOTED" : "")
            + (parent != null ? "\n   parent: " + Integer.toString(parent.getId()) : "")
            + (children != null ? "\n   children: " + children.stream().map(Asset::getId).toList() : "");
    }
}


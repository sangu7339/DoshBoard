package com.VentureBeez.OfferBeez.Admin;
import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name="projects")
@Getter @Setter
public class Project {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, unique=true)
  private String name;

  @Column(name="created_by", nullable=false)
  private Long createdBy;

  @Column(name="created_at", nullable=false)
  private Instant createdAt = Instant.now();
}

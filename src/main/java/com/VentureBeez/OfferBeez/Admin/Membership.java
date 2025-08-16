package com.VentureBeez.OfferBeez.Admin;


import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name="memberships",
  uniqueConstraints = @UniqueConstraint(name="uq_user_project", columnNames={"user_id","project_id"}))
@Getter @Setter
public class Membership {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional=false) @JoinColumn(name="user_id")
  private User user;

  @ManyToOne(optional=false) @JoinColumn(name="project_id")
  private Project project;

  @ManyToOne(optional=false) @JoinColumn(name="department_id")
  private Department department;

  @Column(name="assigned_at", nullable=false)
  private Instant assignedAt = Instant.now();
}

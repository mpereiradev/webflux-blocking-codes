package dev.matheuspereira.webflux.blocking.codes.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("users")
public class User {
  @Id
  private Integer id;
  @Column("full_name")
  private String fullName;
  @Column()
  private String email;
  @Column()
  private String password;
  @Column()
  private String status;
}

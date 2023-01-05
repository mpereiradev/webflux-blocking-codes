package dev.matheuspereira.webflux.blocking.codes.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusUserUpdatedRes {
  private int id;
  private String status;
}

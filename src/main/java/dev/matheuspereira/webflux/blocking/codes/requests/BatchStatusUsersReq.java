package dev.matheuspereira.webflux.blocking.codes.requests;

import lombok.Data;

@Data
public class BatchStatusUsersReq {
  private int[] bachIds;
  private String status;
}

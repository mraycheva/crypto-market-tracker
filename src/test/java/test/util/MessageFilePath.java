package test.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageFilePath {

  private final String BASE_DIRECTORY_PATH =
    "./src/test/resources/message/kraken/original/";

  public final String SUBSCRIPTION_REQUEST =
    BASE_DIRECTORY_PATH + "subscription_request.json";
  public final String ASKS_UPDATE = BASE_DIRECTORY_PATH + "asks_update.json";
  public final String BIDS_UPDATE = BASE_DIRECTORY_PATH + "bids_update.json";
  public final String ASKS_AND_BIDS_UPDATE =
    BASE_DIRECTORY_PATH + "asks_n_bids_update.json";
  public final String ASKS_AND_BIDS_SNAPSHOT =
    BASE_DIRECTORY_PATH + "asks_n_bids_snapshot.json";
  public final String HEARTBEAT = BASE_DIRECTORY_PATH + "heartbeat.json";
  public final String SYSTEM_STATUS =
    BASE_DIRECTORY_PATH + "system_status.json";
  public final String SUBSCRIPTION_STATUS =
    BASE_DIRECTORY_PATH + "subscription_status.json";
}

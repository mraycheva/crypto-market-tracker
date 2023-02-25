package test.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UpdateFilePath {

  private final String BASE_DIRECTORY_PATH =
    "./src/test/resources/message/kraken/";

  public final String ASKS_AND_BIDS_UPDATE_RAW =
    BASE_DIRECTORY_PATH + "asks_n_bids_update_raw.json";
  public final String ASKS_AND_BIDS_SNAPSHOT_RAW =
    BASE_DIRECTORY_PATH + "asks_n_bids_snapshot_raw.json";
  public final String ASKS_STRIPPED =
    BASE_DIRECTORY_PATH + "asks_stripped.json";
  public final String BIDS_STRIPPED =
    BASE_DIRECTORY_PATH + "bids_stripped.json";
}

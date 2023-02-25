package test.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import java.io.File;
import java.nio.file.Paths;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class FileReader {

  private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public String read(final String filePath) {
    val json = callCheckedFunction(() ->
      OBJECT_MAPPER.readTree(getFile(filePath))
    );
    return callCheckedFunction(() -> OBJECT_MAPPER.writeValueAsString(json));
  }

  public <T> T read(final String filePath, final Class<T> type) {
    return callCheckedFunction(() ->
      OBJECT_MAPPER.readValue(getFile(filePath), type)
    );
  }

  public <T> T read(
    final String filePath,
    final TypeReference<T> typeReference
  ) {
    return callCheckedFunction(() ->
      OBJECT_MAPPER.readValue(getFile(filePath), typeReference)
    );
  }

  private <T> T callCheckedFunction(final CheckedFunction0<T> function) {
    return Try.of(function).get();
  }

  private File getFile(final String filePath) {
    return Paths.get(filePath).toFile();
  }
}

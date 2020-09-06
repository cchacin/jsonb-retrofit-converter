package retrofit2.converter.jsonb;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

import javax.json.bind.Jsonb;
import java.io.IOException;
import java.lang.reflect.Type;

final class JsonbRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8");

    private final Jsonb jsonb;
    private final Type type;

    JsonbRequestBodyConverter(
            final Jsonb jsonb,
            final Type type) {
        this.jsonb = jsonb;
        this.type = type;
    }

    @Override
    public RequestBody convert(
            final T value) throws IOException {
        final String json = this.jsonb.toJson(value, this.type);
        return RequestBody.create(MEDIA_TYPE, json.getBytes());
    }
}
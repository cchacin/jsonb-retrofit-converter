package retrofit2.converter.jsonb;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import javax.json.bind.Jsonb;
import java.io.IOException;
import java.lang.reflect.Type;

final class JsonbResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Jsonb jsonb;
    private final Type type;

    JsonbResponseBodyConverter(
            final Jsonb jsonb,
            final Type type) {
        this.jsonb = jsonb;
        this.type = type;
    }

    @Override
    public T convert(
            final ResponseBody value) throws IOException {
        try (value) {
            return this.jsonb.fromJson(value.string(), this.type);
        }
    }
}
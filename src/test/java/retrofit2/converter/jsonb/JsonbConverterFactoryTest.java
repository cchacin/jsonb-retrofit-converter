package retrofit2.converter.jsonb;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import java.io.IOException;
import java.lang.reflect.Type;

public class JsonbConverterFactoryTest implements WithAssertions {

    interface AnInterface {
        String getName();
    }

    static class AnImplementation implements AnInterface {
        private String name;

        AnImplementation() {
        }

        AnImplementation(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    public static class AnInterfaceSerializer implements JsonbSerializer<AnInterface> {

        @Override
        public void serialize(
                final AnInterface anInterface,
                final JsonGenerator generator,
                final SerializationContext ctx) {
            generator.writeStartObject();
            generator.writeKey("name");
            generator.write(anInterface.getName());
            generator.writeEnd();
        }
    }

    public static class AnInterfaceDeserializer implements JsonbDeserializer<AnInterface> {

        @Override
        public AnInterface deserialize(
                final JsonParser parser,
                final DeserializationContext ctx,
                final Type type) {
            while (parser.hasNext()) {
                final JsonParser.Event next = parser.next();

                if (next.equals(JsonParser.Event.KEY_NAME) && parser.getString().equals("name")) {
                    //invokes JSONB processing for a CrateInner as a root type with "shared" instance of JsonParser
                    parser.next();
                    return new AnImplementation(ctx.deserialize(String.class, parser));
                }
            }
            return new AnImplementation(null);
        }
    }

    interface Service {
        @POST("/")
        Call<AnImplementation> anImplementation(@Body AnImplementation impl);

        @POST("/")
        Call<AnInterface> anInterface(@Body AnInterface impl);
    }

    @Rule
    public final MockWebServer server = new MockWebServer();

    private Service service;

    @Before
    public void setUp() {
        final Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl(this.server.url("/"))
                        .addConverterFactory(JsonbConverterFactory.create(
                                JsonbBuilder.create(
                                        new JsonbConfig()
                                                .withDeserializers(new AnInterfaceDeserializer())
                                                .withSerializers(new AnInterfaceSerializer())
                                )
                        ))
                        .build();
        this.service = retrofit.create(Service.class);
    }

    @Test
    public void anInterface() throws IOException, InterruptedException {
        this.server.enqueue(new MockResponse().setBody("{\"name\":\"value\"}"));

        final Call<AnInterface> call = this.service.anInterface(new AnImplementation("value"));
        final Response<AnInterface> response = call.execute();
        final AnInterface body = response.body();
        this.assertThat(body.getName()).isEqualTo("value");

        final RecordedRequest request = this.server.takeRequest();
        this.assertThat(request.getBody().readUtf8()).isEqualTo("{\"name\":\"value\"}");
        this.assertThat(request.getHeader("Content-Type")).isEqualTo("application/json; charset=UTF-8");
    }

    @Test
    public void anImplementation() throws IOException, InterruptedException {
        this.server.enqueue(new MockResponse().setBody("{\"name\":\"value\"}"));

        final Call<AnImplementation> call = this.service.anImplementation(new AnImplementation("value"));
        final Response<AnImplementation> response = call.execute();
        final AnImplementation body = response.body();
        this.assertThat(body.name).isEqualTo("value");

        final RecordedRequest request = this.server.takeRequest();
        this.assertThat(request.getBody().readUtf8()).isEqualTo("{\"name\":\"value\"}");
        this.assertThat(request.getHeader("Content-Type")).isEqualTo("application/json; charset=UTF-8");
    }
}

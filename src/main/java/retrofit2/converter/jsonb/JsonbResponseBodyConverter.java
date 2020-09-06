/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
JakartaEE Json-B Retrofit2 Converter
====================================

A Retrofit 2 `Converter.Factory` for [JakartaEE Json-B][1].


Usage
-----

Add a converter factory when building your `Retrofit` instance using the
`create` method:

```java
var retrofit = new Retrofit.Builder()
    .baseUrl("https://example.com/")
    .addConverterFactory(JsonbConverterFactory.create())
    .build();
```

Alternatively, you can pass an instance of `javax.json.bind.Jsonb`:

```java
var jsonb = JsonbBuilder.create(/* configure your jsonb instance here */ );
var retrofit = new Retrofit.Builder()
    .baseUrl("https://example.com/")
    .addConverterFactory(JsonbConverterFactory.create(jsonb))
    .build();
```


Download
--------

Download [the latest JAR][2] or grab via [Gradle][3]:

```groovy
implementation 'io.github.cchacin:jsonb-retrofit-converter:1.0.2'
```

or [Maven][3]:

```xml
<dependency>
  <groupId>io.github.cchacin</groupId>
  <artifactId>jsonb-retrofit-converter</artifactId>
  <version>1.0.2</version>
</dependency>
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].

License
=======

    Copyright 2020 Carlos Chacin

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 [1]: https://jakarta.ee/specifications/jsonb/1.0/
 [2]: https://search.maven.org/artifact/io.github.cchacin/jsonb-retrofit-converter/1.0.2/jar
 [3]: https://search.maven.org/search?q=g:io.github.cchacin%20a:jsonb-retrofit-converter
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/io/github/cchacin/jsonb-retrofit-converter/

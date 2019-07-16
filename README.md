# example-zipkin

## Run

create a `.env` and set the elasticsearch configuration.

```bash
docker-compose up
```

## Access

- [zipkin ui](localhost:9411)
- [serviceA (dotnet core api)](localhost:5050)

## Zipkin

### Resource

[Elastic Configuration](https://github.com/apache/incubator-zipkin/commit/0a83d9de47c8235d88c9d5cfb96b3b4342654e3a)

## Service-DotNet

Instrumentation: [zipkin4net](https://github.com/openzipkin/zipkin4net)

dotnet core 2.2 configuration

```
dotnet add package zipkin4net --version 1.3.0
dotnet add package zipkin4net.middleware.aspnetcore --version 1.3.0
```

in `startup.cs`

```c#
    var lifetime = app.ApplicationServices.GetService<IApplicationLifetime>();
    lifetime.ApplicationStarted.Register(() => {
        TraceManager.SamplingRate = 1.0f;
        var logger = new TracingLogger(loggerFactory, "zipkin4net");
        var httpSender = new HttpZipkinSender("http://localhost:9411", "application/json");
        var tracer = new ZipkinTracer(httpSender, new JSONSpanSerializer());
        TraceManager.RegisterTracer(tracer);
        TraceManager.Start(logger);
    });
    lifetime.ApplicationStopped.Register(() => TraceManager.Stop());
    app.UseTracing("ServiceA");
```

## Service-GOLANG

Instrumentation: [zipkin-go](https://github.com/openzipkin/zipkin-go)

## Service-Spring

- [spring-cloud-sleuth](https://cloud.spring.io/spring-cloud-sleuth/spring-cloud-sleuth.html)

Maven Config

```xml

    <dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>Greenwich.RELEASE</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>

    ...

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-sleuth</artifactId>
    </dependency>

    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
    </dependency>

    <dependency>
        <groupId>net.logstash.logback</groupId>
        <artifactId>logstash-logback-encoder</artifactId>
        <version>6.1</version>
    </dependency>
```

logback config: see [logback-spring.xml](ServiceJava/service-spring/src/main/resources/logback-spring.xml)

### Run service locally

```bash
cd ServiceGo/src/main
go get
```

```bash
go build && ./main.exe
```

### Resources

- [instrumenting a go application with zipkin](https://medium.com/devthoughts/instrumenting-a-go-application-with-zipkin-b79cc858ac3e)

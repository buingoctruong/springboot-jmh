package io.github.truongbn.jmh;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.web.util.UriComponentsBuilder;

@Fork(value = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 20, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 20, time = 200, timeUnit = TimeUnit.MILLISECONDS)
public class StringJmh {
    String departmentId = "testDepartmentId";
    String employeeId = "testEmployeeId";
    @Benchmark
    public void messageFormat(Blackhole blackhole) {
        String path = MessageFormat.format("/api/v1/department/{0}/employees/{1}", departmentId,
                employeeId);
        blackhole.consume(path);
    }

    @Benchmark
    public void format(Blackhole blackhole) {
        String path = "/api/v1/department/%s/employees/%s".formatted(departmentId, employeeId);
        blackhole.consume(path);
    };

    @Benchmark
    public void concatenation(Blackhole blackhole) {
        String path = "/api/v1/department/" + departmentId + "/employees/" + employeeId;
        blackhole.consume(path);
    }

    @Benchmark
    public void stringBuilder(Blackhole blackhole) {
        String path = new StringBuilder().append("/api/v1/department/").append(departmentId)
                .append("/employees/").append(employeeId).toString();
        blackhole.consume(path);
    }

    @Benchmark
    public void uriComponentBuilder(Blackhole blackhole) {
        String path = UriComponentsBuilder.newInstance()
                .path("/api/v1/department/{0}/employees/{1}")
                .buildAndExpand(departmentId, employeeId).encode().encode().toString();
        blackhole.consume(path);
    }
}

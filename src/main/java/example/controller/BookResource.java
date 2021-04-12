package example.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import example.dto.BookDto;
import example.service.BookService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Api(value="Book Resource Endpoint")
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class BookResource {

    private final BookService bookService;
    Counter getCounter;
    Timer getTimer;

    @Autowired
    public BookResource(BookService bookService, MeterRegistry meterRegistry) {
        this.bookService = bookService;
        getCounter = Counter.builder("get_books_counter").description("Number of GET Requests").register(meterRegistry);
        getTimer = Timer.builder("get_books_timer").description("ResponseTime for GET").register(meterRegistry);
    }

    @ApiOperation(value = "Gets the list of available books")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = List.class)
    })
    @GetMapping(path = "/books", produces = {"application/json"})
    public ResponseEntity<List<BookDto>> list() {
        getCounter.increment();
        log.info("GET /api/v1/books");
        getTimer.record(() -> ResponseEntity.ok(bookService.list()));
        return ResponseEntity.ok(bookService.list());
    }

    @ApiOperation(value = "Retrieves the requested book")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = BookDto.class),
            @ApiResponse(code = 400, message = "Bbook Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 422, message = "Unprocessable Entity"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping(path = "/books/{id}", produces = {"application/json"})
    public ResponseEntity<BookDto> get(@PathVariable("id") Long bookId) {

        log.info("GET /api/v1/books/"+bookId);
        return ResponseEntity.ok(bookService.get(bookId));
    }

    @ApiOperation(value = "Creates a new book")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bbook Request"),
            @ApiResponse(code = 422, message = "Unprocessable Entity"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping(path = "/books", consumes = {"application/json"})
    public ResponseEntity<Void> create(@Valid @RequestBody BookDto bookDto) {

        log.info("POST /api/v1/books : "+bookDto);
        Long bookId = bookService.create(bookDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(bookId).toUri();

        return ResponseEntity.created(location).build();
    }
}

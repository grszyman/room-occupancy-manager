package pl.szymsoft.hotel.occupancy.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import pl.szymsoft.hotel.occupancy.domain.api.OccupancyPlan;
import pl.szymsoft.hotel.occupancy.domain.api.OccupancyPlanner;
import pl.szymsoft.hotel.occupancy.spring.WebConfig;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Import(WebConfig.class) // it explicitly shows module dependencies, and it is helpful in tests to set up the limited context
class OccupancyPlannerRouter {

    @Bean
    RouterFunction<ServerResponse> routes(OccupancyPlanner planner, Validator validator) {

        final var handler = new Handler(planner, validator);

        return route(POST("/occupancy-plans").and(accept(APPLICATION_JSON)), handler::getOccupationPlanFor);
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class Handler {

        @NonNull
        private final OccupancyPlanner occupancyPlanner;

        @NonNull
        private final Validator validator;

        public Mono<ServerResponse> getOccupationPlanFor(ServerRequest serverRequest) {

            return serverRequest.bodyToMono(OccupancyPlanRequest.class)
                    .doOnNext(this::validate)
                    .map(this::createOccupancyPlan)
                    .map(OccupancyPlanResponse::from)
                    .flatMap(Handler::toServerResponse);
        }

        private void validate(OccupancyPlanRequest request) {
            final var errors = new BeanPropertyBindingResult(request, "request");
            validator.validate(request, errors);
            if (errors.hasErrors()) {
                throw new ServerWebInputException(errors.toString()); // that should be done in a better way
            }
        }

        private static Mono<ServerResponse> toServerResponse(OccupancyPlanResponse response) {
            return ServerResponse.ok()
                    .contentType(APPLICATION_JSON)
                    .body(BodyInserters.fromValue(response));
        }

        private OccupancyPlan createOccupancyPlan(OccupancyPlanRequest request) {
            return occupancyPlanner.createPlanFor(
                    request.premiumRooms(),
                    request.economyRooms());
        }
    }
}

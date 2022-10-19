package org.acme.opentracing.services;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import org.eclipse.microprofile.opentracing.Traced;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

@Traced(operationName = "IngestorService")
@ApplicationScoped
public class IngestorService {

    @Inject
    Vertx vertx;

    FileSystem fileSystem;

    public IngestorService(Vertx vertx) {
        this.vertx = vertx;
        fileSystem = vertx.fileSystem();
    }

    String WORKING_DIR = "/tmp/";  //TODO via configuraties
    String INBOX = WORKING_DIR.concat("inbox");
    String DONE = WORKING_DIR.concat("done");

    static final Logger LOGGER = Logger.getLogger(IngestorService.class);

    public record Content(String header, List<String> pipeDelimitedContent, String footer) implements Serializable {

        public Content { ///record validatie
            if (obtainExpectedRows(footer) != pipeDelimitedContent.size()) {
                throw new IllegalArgumentException("Incompleet pipeDelimitedContent");
            }
        }

        private int obtainExpectedRows(String footer) {
            //" 20 records"
            return Arrays.stream(footer.split(" "))
                    .filter(s -> {
                        try {
                            Integer.parseInt(s);
                            return true;
                        } catch (NumberFormatException e) {
                            LOGGER.warn("Input String cannot be parsed to Integer.");
                            return false;
                        }
                    })
                    .map(Integer::parseInt)
                    .findFirst()
                    .orElse(0);
        }
    }


    public CompletionStage<String> obtainData() {
        return hasPendingFilesToIngest()
                .compose(fileFullPath -> fileSystem.readFile(fileFullPath)
                        .map(read -> {
                            final Content content = obtainMutatiesFromBuffer(read);
                            LOGGER.info(content);
                            fileSystem.move(fileFullPath, DONE + "/" + UUID.randomUUID() + ".csv"); //TODO origineel filename
                            return content;
                        }))
                .toCompletionStage().thenApply(content -> content.pipeDelimitedContent.toString());
    }

    private Content obtainMutatiesFromBuffer(final Buffer buffer) {
        final List<String> fullContent = Arrays.stream(buffer.toString().split("\n")).toList();
        final String header = fullContent.get(0);
        //2 (header + cvs header rows)
        final List<String> content = fullContent.subList(2, fullContent.size() - 1);
        final String footer = fullContent.get(fullContent.size() - 1);
        return new Content(header, content, footer);
    }


    private Future<String> hasPendingFilesToIngest() {
        return fileSystem
                .readDir(INBOX)
                .compose(x -> {
                    if (x.isEmpty()) {
                        final String message = "Niets te doen %s is leeg".formatted(INBOX);
                        LOGGER.error(message);
                        return Future.failedFuture(message);
                    }
                    final String result = x.get(0);//voorlopig de eerste TODO
                    LOGGER.info("found %s".formatted(result));
                    return Future.succeededFuture(result);
                });
    }

}

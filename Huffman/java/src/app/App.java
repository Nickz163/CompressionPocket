package app;

import coder.Coder;
import coder.CoderRegistry;
import coder.CoderRegistryFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;

public class App {
    private static final char COMMAND_PREFIX = '-';
    private static final String COMMAND_COMPRESS = "-c";
    private static final String COMMAND_DECOMPRESS = "-d";
    private static final String COMMAND_TEST = "-t";
    private static final String COMMAND_VIEW = "-v";

    private static final CoderRegistry registry = CoderRegistryFactory.create();

    public static void main(String[] args) throws Exception {
        Map<Boolean, List<String>> params = Arrays.stream(args)
                .collect(groupingBy(arg -> arg.charAt(0) == COMMAND_PREFIX));

        List<String> commands = params.get(true);
        List<String> operands = params.get(false);

        if (commands == null || operands == null) {
            System.err.println("Bad params");
            return;
        }

        Coder coder = commands.stream()
                .map(registry::get)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new Exception("Algorithm param no found"));

        if (commands.contains(COMMAND_TEST)) {
            coder.checkHeader(operands.get(0));
            System.out.println("Ok");

        } else if (commands.contains(COMMAND_VIEW)) {
            coder.showContent(operands.get(0));

        } else {
            if (commands.contains(COMMAND_COMPRESS)) {
                if (operands.size() < 2) {
                    System.err.println("Bad params");
                } else {
                    String archiveName = operands.remove(operands.size() - 1);
                    coder.compress(operands, archiveName);
                }

            } else if (commands.contains(COMMAND_DECOMPRESS)) {
                coder.decompress(operands.get(0));
            } else {
                System.err.println("Bad params");
            }
        }
    }
}

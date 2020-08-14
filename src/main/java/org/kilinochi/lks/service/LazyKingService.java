package org.kilinochi.lks.service;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author arman.shamenov
 */
public class LazyKingService {

    private static final Pattern COLON_PATTERN = Pattern.compile(":");

    public DefaultDirectedGraph<String, DefaultEdge> shift(List<String> dataset) {
        DefaultDirectedGraph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        Set<String> bosses = new TreeSet<>();
        Set<String> slaves = new TreeSet<>();

        dataset.stream().map(String::trim).forEach(data -> {
            Matcher colonMatcher = COLON_PATTERN.matcher(data);

            if (!colonMatcher.find()) {
                if (graph.containsVertex(data)) {
                    return;
                }
                bosses.add(data);
                return;
            }


            int startPos = colonMatcher.start();
            String boss = data.substring(0, startPos).trim();

            Arrays.stream(data.substring(startPos + 1).split(","))
                    .map(String::trim)
                    .forEach(slave -> {
                        if (bosses.contains(slave)) {
                            bosses.remove(slave);
                        }

                        graph.addVertex(boss);
                        graph.addVertex(slave);
                        graph.addEdge(boss, slave);
                        slaves.add(slave);
                    });

            bosses.add(boss);
        });

        bosses.stream()
                .filter(boss -> !slaves.contains(boss))
                .forEach(boss -> {
                    graph.addVertex("Король");
                    graph.addVertex(boss);
                    graph.addEdge("Король", boss);
                });


        return graph;
    }
}

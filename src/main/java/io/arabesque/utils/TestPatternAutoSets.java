package io.arabesque.utils;

import io.arabesque.conf.Configuration;
import io.arabesque.conf.TestConfiguration;
import io.arabesque.graph.MainGraph;
import io.arabesque.pattern.JBlissPattern;
import io.arabesque.pattern.VertexPositionEquivalences;
import net.openhft.koloboke.collect.set.hash.HashIntSet;
import net.openhft.koloboke.collect.set.hash.HashIntSets;

import java.util.ArrayList;

public class TestPatternAutoSets {
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("No graph given");
        }

        if (args.length < 2) {
            throw new IllegalArgumentException("No embedding given");
        }

        Configuration.setIfUnset(new TestConfiguration(args[0]));

        String embeddingStr = args[1];

        String[] edgesStr = embeddingStr.split(" ");

        HashIntSet vertexIds = HashIntSets.newMutableSet();
        IntArrayList edgeIds = new IntArrayList(edgesStr.length);
        ArrayList<IntIntPair> edges = new ArrayList<>(edgesStr.length);

        MainGraph mainGraph = Configuration.get().getMainGraph();

        for (String edgeStr : edgesStr) {
            String[] edgeComponentsStr = edgeStr.split("-");

            int srcId = Integer.parseInt(edgeComponentsStr[0]);
            int dstId = Integer.parseInt(edgeComponentsStr[1]);

            edges.add(new IntIntPair(srcId, dstId));

            vertexIds.add(srcId);
            vertexIds.add(dstId);

            System.out.println("Found edge " + srcId + ", " + dstId);

            int edgeId = mainGraph.getEdgeId(srcId, dstId);

            edgeIds.add(edgeId);
        }

        JBlissPattern jblissPattern = new JBlissPattern();
        //BasicPattern basicPattern = new BasicPattern();

        //basicPattern.setupStructures(vertexIds.size(), edgeIds.getSize());

        for (int i = 0; i < edgeIds.getSize(); ++i) {
            int edgeId = edgeIds.getUnchecked(i);
            IntIntPair edge = edges.get(i);
            jblissPattern.addEdge(edgeId);
            //basicPattern.addEdge(edge.getFirst(), edge.getSecond());
        }

        //basicPattern.turnCanonical();

        printVertexPositionEquivalences("jbliss", jblissPattern.getVertexPositionEquivalences());
        jblissPattern.turnCanonical();
        printVertexPositionEquivalences("jbliss-min", jblissPattern.getVertexPositionEquivalences());
        //printVertexPositionEquivalences("basic", basicPattern.getVertexPositionEquivalences());
    }

    public static void printVertexPositionEquivalences(String title, VertexPositionEquivalences vertexPositionEquivalences) {
        System.out.println("Autovertex set of " + title);
        System.out.println(vertexPositionEquivalences.toString());
    }
}
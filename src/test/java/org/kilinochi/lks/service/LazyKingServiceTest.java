package org.kilinochi.lks.service;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.util.mxCellRenderer;
import org.hamcrest.Matchers;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

class LazyKingServiceTest {

    final static List<String> DATASET = List.of(
            "служанка Аня",
            "управляющий Семен Семеныч: крестьянин Федя, доярка Нюра",
            "дворянин Кузькин: управляющий Семен Семеныч, жена Кузькина, экономка Лидия Федоровна",
            "экономка Лидия Федоровна: дворник Гена, служанка Аня",
            "доярка Нюра",
            "кот Василий: человеческая особь Катя",
            "дворник Гена: посыльный Тошка",
            "киллер Гена",
            "зажиточный холоп: крестьянка Таня",
            "секретарь короля: зажиточный холоп, шпион Т",
            "шпион Т: кучер Д",
            "посыльный Тошка: кот Василий",
            "аристократ Клаус",
            "просветленный Антон"
    );

    private LazyKingService service;

    @BeforeEach
    void setUp() {
        service = new LazyKingService();
    }

    @Test
    void shift() throws IOException {
        DefaultDirectedGraph<String, DefaultEdge> graph = service.shift(DATASET);

        File imgFile = new File("src/test/resources/graph.png");
        imgFile.createNewFile();
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);
        mxCircleLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 3, Color.WHITE, true, null);
        ImageIO.write(image, "PNG", imgFile);

        Set<String> vertexSet = graph.vertexSet();

        assertThat(vertexSet, Matchers.hasItems("управляющий Семен Семеныч",
                "крестьянин Федя",
                "доярка Нюра",
                "дворянин Кузькин",
                "жена Кузькина",
                "экономка Лидия Федоровна",
                "дворник Гена",
                "служанка Аня", "кот Василий",
                "человеческая особь Катя",
                "посыльный Тошка",
                "секретарь короля",
                "шпион Т",
                "кучер Д",
                "Король",
                "аристократ Клаус",
                "киллер Гена",
                "просветленный Антон"));
    }
}
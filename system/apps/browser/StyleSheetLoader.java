package com.example;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeBasedTable;
import com.google.common.reflect.TypeToken;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Loads and parses the style sheet from a given URL.
 */
public class StyleSheetLoader {

    /**
     * Parses the CSS file loaded from the specified URL into a map format.
     * Keys represent CSS selector strings, and values correspond to CSS rule lists.
     */
    public Multimap<String, String> parseStylesheet(String cssUrl) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(Jsoup.connect(cssUrl).ignoreContentType(true).execute().bodyAsBytes());
        Document doc = Jsoup.parse(stream, null, cssUrl);
        return parseRuleSets(doc.select("style, link[href][rel='stylesheet']"), "", "");
    }

    /**
     * Recursively parses the passed element nodes and combines their CSS rules.
     *
     * @param parent Node holding child node(s) to be processed recursively.
     * @param prefix Prefix to combine with the selector string.
     * @param suffix Suffix to combine with the selector string.
     * @return Combined CSS rules represented as a {@link Multimap}.
     */
    private Multimap<String, String> parseRuleSets(Elements parent, String prefix, String suffix) {
        Multimap<String, String> rules = TreeBasedTable.create();

        for (Element el : parent) {
            String selector = prefix + el.nodeName() + suffix;
            if (el.hasAttr("class")) {
                for (String className : el.className().split(" ")) {
                    selector += "." + className;
                }
            }

            if (el.hasAttr("id")) {
                selector += "#" + el.id();
            }

            if (el.attributes().hasKey("style")) {
                rules.putAll(selector, Collections.singletonList(el.attr("style")));
            }

            if (el instanceof Elements) {
                rules.putAll(parseRuleSets(((Element) el).children(), ":not(:first-child)", selector + "+"));
            }
        }

        return rules;
    }
}

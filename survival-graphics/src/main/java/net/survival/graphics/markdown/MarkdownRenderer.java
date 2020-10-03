package net.survival.graphics.markdown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import net.survival.markdown.DivisionElement;
import net.survival.markdown.MarkupElement;
import net.survival.markdown.Page;
import net.survival.markdown.TextElement;

public final class MarkdownRenderer {
    private MarkdownRenderer() {}

    public static RenderedPage printPage(Page page, double width, double height) {
        var printedElements = new ArrayList<RenderedElement>();

        var elementParents = getElementParents(page);

        var nextElements = (Queue<MarkupElement>) new LinkedList<MarkupElement>();
        nextElements.add(page.getRoot());

        while (!nextElements.isEmpty()) {
            var element = nextElements.remove();

            if (element instanceof DivisionElement) {
                var divisionElement = (DivisionElement) element;

                nextElements.addAll(divisionElement.getChildren());
            }
            else if (element instanceof TextElement) {
            }
            else {
                throw new RuntimeException("Unknown element type.");
            }
        }

        return new RenderedPage(printedElements, width, height);
    }

    private static HashMap<MarkupElement, MarkupElement> getElementParents(Page page) {
        var result = new HashMap<MarkupElement, MarkupElement>();
        var nextElements = new Stack<MarkupElement>();
        nextElements.push(page.getRoot());

        while (!nextElements.isEmpty()) {
            var element = nextElements.pop();

            if (element instanceof DivisionElement) {
                var divisionElement = (DivisionElement) element;

                for (var child : divisionElement.getChildren()) {
                    result.put(child, divisionElement);
                    nextElements.push(child);
                }
            }
        }

        result.put(page.getRoot(), null);
        return result;
    }
}
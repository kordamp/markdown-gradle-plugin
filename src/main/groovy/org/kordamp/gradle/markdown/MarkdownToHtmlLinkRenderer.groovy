package org.kordamp.gradle.markdown

import org.pegdown.LinkRenderer
import org.pegdown.ast.AutoLinkNode
import org.pegdown.ast.ExpLinkNode
import org.pegdown.ast.RefLinkNode
import org.pegdown.ast.WikiLinkNode

import static org.pegdown.LinkRenderer.Rendering

/**
 * @author Ed MacDonald
 */
class MarkdownToHtmlLinkRenderer extends LinkRenderer {
    private Conversion conversion = Conversion.MARKDOWN

    @Override
    Rendering render(AutoLinkNode node) {
        return adjustHref(super.render(node))
    }

    @Override
    Rendering render(ExpLinkNode node, String text) {
        return adjustHref(super.render(node, text))
    }

    @Override
    Rendering render(RefLinkNode node, String url, String title, String text) {
        return adjustHref(super.render(node, url, title, text))
    }

    @Override
    Rendering render(WikiLinkNode node) {
        return adjustHref(super.render(node))
    }

    // Assumes each extension starts with "."
    private Rendering adjustHref( Rendering rendering ) {
        if(conversion.extensions().size() > 0) {
            def regex =  conversion.extensions().join("|\\")
            regex = "(\\" + regex + ")\$"
            rendering.href = rendering.href.replaceFirst(regex, conversion.targetExtension())
        }
        return rendering
    }
}

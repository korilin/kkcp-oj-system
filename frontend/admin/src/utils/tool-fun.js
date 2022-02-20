import { marked } from "marked";

export function resolveMarkdownAsHtml(md) {
    const html = marked(md);
    return html;
}
